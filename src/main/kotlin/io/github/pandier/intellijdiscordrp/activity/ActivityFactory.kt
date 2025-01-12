package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.settings.ImageSetting
import io.github.pandier.intellijdiscordrp.settings.LogoStyleSetting
import io.github.pandier.intellijdiscordrp.settings.TimestampTargetSetting
import io.github.pandier.intellijdiscordrp.util.urlRegex
import io.github.vyfor.kpresence.rpc.Activity
import io.github.vyfor.kpresence.rpc.activity
import java.time.Instant

private fun ImageSetting.getIcon(context: ActivityContext, logoStyle: LogoStyleSetting) = when (this) {
    ImageSetting.APPLICATION -> when (logoStyle) {
        LogoStyleSetting.MODERN -> currentActivityApplicationType.modernIcon
        LogoStyleSetting.CLASSIC -> currentActivityApplicationType.classicIcon
    }
    ImageSetting.FILE -> context.file?.type?.icon
}

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
    private val details: String,
    private val state: String,
    private val largeImage: ImageSetting?,
    private val largeImageText: String,
    private val smallImage: ImageSetting?,
    private val smallImageText: String,
    private val buttonText: String?,
    private val buttonUrl: String,
    private val timestampEnabled: Boolean,
    private val timestampTarget: TimestampTargetSetting,
) {
    fun create(context: ActivityContext): Activity = activity {
        details = this@ActivityFactory.details.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }
        state = this@ActivityFactory.state.ifEmpty { null }?.let { displayMode.format(it, context).fitToRange(2, 128) }

        assets {
            if (this@ActivityFactory.largeImage != null) {
                largeImage = this@ActivityFactory.largeImage.getIcon(context, logoStyle)
                largeText = displayMode.format(this@ActivityFactory.largeImageText, context).fitToRange(2, 128)
            }

            if (this@ActivityFactory.smallImage != null) {
                smallImage = this@ActivityFactory.smallImage.getIcon(context, logoStyle)
                smallText = displayMode.format(this@ActivityFactory.smallImageText, context).fitToRange(2, 128)
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
