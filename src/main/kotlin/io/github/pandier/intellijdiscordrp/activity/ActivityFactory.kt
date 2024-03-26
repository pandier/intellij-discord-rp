package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.IconTypeSetting

private fun IconTypeSetting.getIcon(info: ActivityContext) = when (this) {
    IconTypeSetting.APPLICATION -> currentActivityApplicationType.icon
    IconTypeSetting.FILE -> info.file?.type?.icon
}

class ActivityFactory(
    private val details: String = "",
    private val state: String = "",
    private val largeImage: IconTypeSetting? = null,
    private val largeImageText: String = "",
    private val smallImage: IconTypeSetting? = null,
    private val smallImageText: String = "",
) {
    fun create(info: ActivityContext): Activity = Activity().also {
        if (details.isNotEmpty())
            it.details = info.format(details)
        if (state.isNotEmpty())
            it.state = info.format(state)

        if (largeImage != null && largeImageText.isNotEmpty()) {
            it.assets().largeImage = largeImage.getIcon(info)
            it.assets().largeText = info.format(largeImageText)
        }

        if (smallImage != null && smallImageText.isNotEmpty()) {
            it.assets().smallImage = smallImage.getIcon(info)
            it.assets().smallText = info.format(smallImageText)
        }

        it.timestamps().start = info.start
    }
}