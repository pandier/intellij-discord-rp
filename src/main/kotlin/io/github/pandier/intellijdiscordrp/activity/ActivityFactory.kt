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

class ActivityFactory(
    private val displayMode: ActivityDisplayMode,
    private val logoStyle: LogoStyleSetting,
    private val details: String,
    private val state: String,
    private val largeImage: ImageSetting?,
    private val largeImageText: String,
    private val smallImage: ImageSetting?,
    private val smallImageText: String,
    private val timestampEnabled: Boolean,
) {
    fun create(context: ActivityContext): Activity = activity {
        details = this@ActivityFactory.details.ifEmpty { null }?.let { displayMode.format(it, context).padEnd(2) }
        state = this@ActivityFactory.state.ifEmpty { null }?.let { displayMode.format(it, context).padEnd(2) }

        assets {
            if (this@ActivityFactory.largeImage != null) {
                largeImage = this@ActivityFactory.largeImage.getIcon(context, logoStyle)
                largeText = displayMode.format(this@ActivityFactory.largeImageText, context).padEnd(2)
            }

            if (this@ActivityFactory.smallImage != null) {
                smallImage = this@ActivityFactory.smallImage.getIcon(context, logoStyle)
                smallText = displayMode.format(this@ActivityFactory.smallImageText, context).padEnd(2)
            }
        }

        if (context.projectRepositoryUrl != null) {
            button("View Repository", context.projectRepositoryUrl)
        }

        timestamps {
            if (timestampEnabled)
                start = context.start.toEpochMilli()
        }
    }
}