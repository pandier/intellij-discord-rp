package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.ImageSetting

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
) {
    fun create(context: ActivityContext): Activity = Activity().also {
        if (details.isNotBlank())
            it.details = displayMode.format(details, context)
        if (state.isNotBlank())
            it.state = displayMode.format(state, context)

        if (largeImage != null && largeImageText.isNotBlank()) {
            it.assets().largeImage = largeImage.getIcon(context)
            it.assets().largeText = displayMode.format(largeImageText, context)
        }

        if (smallImage != null && smallImageText.isNotBlank()) {
            it.assets().smallImage = smallImage.getIcon(context)
            it.assets().smallText = displayMode.format(smallImageText, context)
        }

        it.timestamps().start = context.start
    }
}