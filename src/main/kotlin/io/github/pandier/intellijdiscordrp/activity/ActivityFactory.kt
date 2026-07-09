package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.kpresence.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import io.github.pandier.intellijdiscordrp.settings.IconType
import io.github.pandier.intellijdiscordrp.settings.LogoStyleSetting
import io.github.pandier.intellijdiscordrp.settings.TimestampTargetSetting
import io.github.pandier.intellijdiscordrp.template.node.TemplateNode
import io.github.pandier.intellijdiscordrp.util.urlRegex
import java.time.Instant

private fun TimestampTargetSetting.getStart(context: ActivityContext): Instant = when (this) {
    TimestampTargetSetting.APPLICATION -> context.appStart
    TimestampTargetSetting.PROJECT -> context.projectStart
    TimestampTargetSetting.FILE -> context.file?.start ?: context.projectStart
}

private fun String.fitToRange(min: Int, max: Int): String =
    if (this.length > max) substring(0, max - 3) + "..." else padEnd(min)

class ActivityFactory(
    private val displayMode: ActivityDisplayMode,
    private val modeSettings: DiscordSettings.Mode,
    private val logoStyle: LogoStyleSetting,
    private val projectIcon: String?,
    private val buttonTextNode: TemplateNode?,
    private val buttonUrlNode: TemplateNode?,
) {
    fun create(context: ActivityContext): Activity = Activity {
        name = modeSettings.nameNode.resolve(context, displayMode).fitToRange(1, 128)

        details = modeSettings.detailsNode.resolve(context, displayMode).fitToRange(2, 128)
        state = modeSettings.stateNode.resolve(context, displayMode).fitToRange(2, 128)

        assets {
            resolveIcon(context, modeSettings.largeIcon)?.let { (image, tooltipNode) ->
                largeImage = image
                largeText = tooltipNode.resolve(context, displayMode).fitToRange(2, 128)
            }

            resolveIcon(context, modeSettings.smallIcon)?.let { (image, tooltipNode) ->
                smallImage = image
                smallText = tooltipNode.resolve(context, displayMode).fitToRange(2, 128)
            }
        }

        if (buttonTextNode != null && buttonUrlNode != null) {
            val formattedButtonUrl = buttonUrlNode.resolve(context, displayMode).fitToRange(1, 512)
            if (urlRegex.matches(formattedButtonUrl)) {
                button(buttonTextNode.resolve(context, displayMode).fitToRange(1, 32), formattedButtonUrl)
            }
        }

        timestamps {
            start = modeSettings.timestampTarget.getStart(context).toEpochMilli()
        }
    }

    private fun resolveIcon(
        context: ActivityContext,
        iconSettings: DiscordSettings.Icon,
    ): Pair<String, TemplateNode>? = when (iconSettings.type) {
        IconType.APPLICATION -> when (logoStyle) {
            LogoStyleSetting.MODERN -> currentActivityApplicationType.modernIcon to iconSettings.tooltipNode
            LogoStyleSetting.CLASSIC -> currentActivityApplicationType.classicIcon to iconSettings.tooltipNode
        }
        IconType.FILE -> context.file?.type?.icon?.let { it to iconSettings.tooltipNode }
        IconType.PROJECT -> projectIcon?.takeIf { it.isNotEmpty() }?.let { it to iconSettings.tooltipNode }
            ?: resolveIcon(context, DiscordSettings.Icon(type = iconSettings.altType, tooltip = iconSettings.altTooltip, altType = IconType.HIDDEN)) // TODO: this invalidates templates
        else -> null
    }
}
