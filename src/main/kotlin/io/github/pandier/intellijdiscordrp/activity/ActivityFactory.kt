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
    private val details: String = "",
    private val state: String = "",
    private val largeImage: ImageSetting? = null,
    private val largeImageText: String = "",
    private val smallImage: ImageSetting? = null,
    private val smallImageText: String = "",
    private val timestampEnabled: Boolean = true,
) {
    fun create(context: ActivityContext): Activity = activity {
        if (this@ActivityFactory.details.isNotBlank())
            details = displayMode.format(this@ActivityFactory.details, context)
        if (this@ActivityFactory.state.isNotBlank())
            state = displayMode.format(this@ActivityFactory.state, context)

        assets {
            if (this@ActivityFactory.largeImage != null && this@ActivityFactory.largeImageText.isNotBlank()) {
                largeImage = this@ActivityFactory.largeImage.getIcon(context)
                largeText = displayMode.format(this@ActivityFactory.largeImageText, context)
            }

            if (this@ActivityFactory.smallImage != null && this@ActivityFactory.smallImageText.isNotBlank()) {
                smallImage = this@ActivityFactory.smallImage.getIcon(context)
                smallText = displayMode.format(this@ActivityFactory.smallImageText, context)
            }
        }

        timestamps {
            if (timestampEnabled)
                start = context.start.toEpochMilli()
        }
    }
}