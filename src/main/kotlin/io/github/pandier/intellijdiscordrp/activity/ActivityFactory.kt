package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.settings.ImageSetting
import io.github.vyfor.kpresence.rpc.Activity
import io.github.vyfor.kpresence.rpc.activity

private fun ImageSetting.getIcon(context: ActivityContext) = when (this) {
    ImageSetting.APPLICATION -> currentActivityApplicationType.icon
    ImageSetting.FILE -> context.file?.type?.icon
}

class ActivityFactory(
    private val displayMode: ActivityDisplayMode,
    private val details: String,
    private val state: String,
    private val largeImage: ImageSetting?,
    private val largeImageText: String,
    private val smallImage: ImageSetting?,
    private val smallImageText: String,
    private val timestampEnabled: Boolean,
) {
    fun create(context: ActivityContext): Activity = activity {
        details = this@ActivityFactory.details.ifEmpty { null }?.let { displayMode.format(it, context) }
        state = this@ActivityFactory.state.ifEmpty { null }?.let { displayMode.format(it, context) }

        assets {
            if (this@ActivityFactory.largeImage != null) {
                largeImage = this@ActivityFactory.largeImage.getIcon(context)
                largeText = displayMode.format(this@ActivityFactory.largeImageText, context)
            }

            if (this@ActivityFactory.smallImage != null) {
                smallImage = this@ActivityFactory.smallImage.getIcon(context)
                smallText = displayMode.format(this@ActivityFactory.smallImageText, context)
            }
        }

        if (context.projectRepositoryUrl != null) {
            button("Repository", context.projectRepositoryUrl)
        }

        timestamps {
            if (timestampEnabled)
                start = context.start.toEpochMilli()
        }
    }
}