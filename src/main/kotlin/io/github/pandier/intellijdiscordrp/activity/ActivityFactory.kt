package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import io.github.pandier.intellijdiscordrp.settings.IconType
import io.github.pandier.intellijdiscordrp.settings.LogoStyleSetting
import io.github.pandier.intellijdiscordrp.settings.TimestampTargetSetting
import io.github.pandier.intellijdiscordrp.util.urlRegex
import io.github.vyfor.kpresence.rpc.Activity
import io.github.vyfor.kpresence.rpc.activity
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
    private val buttonText: String?,
    private val buttonUrl: String,
) {
    private fun resolveIcon(
        context: ActivityContext,
        iconSettings: DiscordSettings.Icon,
    ): Pair<String, String>? = when (iconSettings.type) {
        IconType.APPLICATION -> when (logoStyle) {
            LogoStyleSetting.MODERN -> currentActivityApplicationType.modernIcon to iconSettings.tooltip
            LogoStyleSetting.CLASSIC -> currentActivityApplicationType.classicIcon to iconSettings.tooltip
        }
        IconType.FILE -> context.file?.type?.icon?.let { it to iconSettings.tooltip }
        IconType.PROJECT -> projectIcon?.takeIf { it.isNotEmpty() }?.let { it to iconSettings.tooltip }
            ?: resolveIcon(context, DiscordSettings.Icon(type = iconSettings.altType, tooltip = iconSettings.altTooltip, altType = IconType.HIDDEN))
        else -> null
    }

    fun create(context: ActivityContext): Activity = activity {
        details = modeSettings.details.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }
        state = modeSettings.state.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }

        assets {
            resolveIcon(context, modeSettings.largeIcon)?.let { (image, text) ->
                largeImage = image
                largeText = displayMode.format(text, context).fitToRange(2, 128)
            }

            resolveIcon(context, modeSettings.smallIcon)?.let { (image, text) ->
                smallImage = image
                smallText = displayMode.format(text, context).fitToRange(2, 128)
            }
        }

        if (buttonText != null) {
            val formattedButtonUrl = displayMode.format(buttonUrl, context)
            if (urlRegex.matches(formattedButtonUrl)) {
                button(displayMode.format(buttonText, context).fitToRange(2, 31), formattedButtonUrl)
            }
        }

        timestamps {
            if (modeSettings.timestampEnabled) {
                start = modeSettings.timestampTarget.getStart(context).toEpochMilli()
            }
        }
    }
}
