package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.settings.ImageSetting
import io.github.pandier.intellijdiscordrp.settings.LogoStyleSetting
import io.github.vyfor.kpresence.rpc.Activity
import io.github.vyfor.kpresence.rpc.activity

private fun ImageSetting.getIcon(context: ActivityContext, logoStyle: LogoStyleSetting) = when (this) {
    ImageSetting.APPLICATION -> when (logoStyle) {
        LogoStyleSetting.MODERN -> currentActivityApplicationType.modernIcon
        LogoStyleSetting.CLASSIC -> currentActivityApplicationType.classicIcon
    }
    ImageSetting.FILE -> context.file?.type?.icon
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
    private val repoButtonText: String?,
    private val timestampEnabled: Boolean,
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

        if (repoButtonText != null && context.projectRepositoryUrl != null) {
            button(displayMode.format(repoButtonText, context).fitToRange(2, 31), context.projectRepositoryUrl)
        }

        timestamps {
            if (timestampEnabled)
                start = context.start.toEpochMilli()
        }
    }
}
