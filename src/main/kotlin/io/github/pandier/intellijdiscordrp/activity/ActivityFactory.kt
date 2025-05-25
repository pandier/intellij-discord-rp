package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.settings.IconSetting
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
    private val logoStyle: LogoStyleSetting,
    private val projectIcon: String?,
    private val details: String,
    private val state: String,
    private val largeIcon: IconSetting,
    private val largeIconTooltip: String,
    private val largeIconAlt: IconSetting,
    private val largeIconAltTooltip: String,
    private val smallIcon: IconSetting,
    private val smallIconTooltip: String,
    private val smallIconAlt: IconSetting,
    private val smallIconAltTooltip: String,
    private val buttonText: String?,
    private val buttonUrl: String,
    private val timestampEnabled: Boolean,
    private val timestampTarget: TimestampTargetSetting,
) {
    private fun resolveIcon(
        context: ActivityContext,
        icon: IconSetting,
        tooltip: String,
        altIcon: IconSetting?,
        altTooltip: String
    ): Pair<String, String>? = when (icon) {
        IconSetting.APPLICATION -> when (logoStyle) {
            LogoStyleSetting.MODERN -> currentActivityApplicationType.modernIcon to tooltip
            LogoStyleSetting.CLASSIC -> currentActivityApplicationType.classicIcon to tooltip
        }
        IconSetting.FILE -> context.file?.type?.icon?.let { it to tooltip }
        IconSetting.PROJECT -> projectIcon?.takeIf { it.isNotEmpty() }?.let { it to tooltip }
            ?: altIcon?.let { resolveIcon(context, it, altTooltip, null, "") }
        else -> null
    }

    fun create(context: ActivityContext): Activity = activity {
        details = this@ActivityFactory.details.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }
        state = this@ActivityFactory.state.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }

        assets {
            resolveIcon(context, largeIcon, largeIconTooltip, largeIconAlt, largeIconAltTooltip)?.let { (image, text) ->
                largeImage = image
                largeText = displayMode.format(text, context).fitToRange(2, 128)
            }

            resolveIcon(context, smallIcon, smallIconTooltip, smallIconAlt, smallIconAltTooltip)?.let { (image, text) ->
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
            if (timestampEnabled) {
                start = timestampTarget.getStart(context).toEpochMilli()
            }
        }
    }
}
