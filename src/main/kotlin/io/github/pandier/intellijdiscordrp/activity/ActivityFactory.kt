package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.ImageSetting

private fun ImageSetting.getIcon(info: ActivityContext) = when (this) {
    ImageSetting.APPLICATION -> currentActivityApplicationType.icon
    ImageSetting.FILE -> info.file?.type?.icon
}

class ActivityFactory(
    private val displayMode: ActivityDisplayMode,
    private val details: String = "",
    private val state: String = "",
    private val largeImage: ImageSetting? = null,
    private val largeImageText: String = "",
    private val smallImage: ImageSetting? = null,
    private val smallImageText: String = "",
) {
    fun create(info: ActivityContext): Activity = Activity().also {
        if (details.isNotBlank())
            it.details = info.format(displayMode, details)
        if (state.isNotBlank())
            it.state = info.format(displayMode, state)

        if (largeImage != null && largeImageText.isNotBlank()) {
            it.assets().largeImage = largeImage.getIcon(info)
            it.assets().largeText = info.format(displayMode, largeImageText)
        }

        if (smallImage != null && smallImageText.isNotBlank()) {
            it.assets().smallImage = smallImage.getIcon(info)
            it.assets().smallText = info.format(displayMode, smallImageText)
        }

        it.timestamps().start = info.start
    }
}