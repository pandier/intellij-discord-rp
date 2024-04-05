package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import de.jcm.discordgamesdk.activity.ActivityButton
import de.jcm.discordgamesdk.activity.ActivityButtonsMode
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
        if (details.isNotEmpty())
            it.details = info.format(displayMode, details)
        if (state.isNotEmpty())
            it.state = info.format(displayMode, state)

        if (largeImage != null && largeImageText.isNotEmpty()) {
            it.assets().largeImage = largeImage.getIcon(info)
            it.assets().largeText = info.format(displayMode, largeImageText)
        }

        if (smallImage != null && smallImageText.isNotEmpty()) {
            it.assets().smallImage = smallImage.getIcon(info)
            it.assets().smallText = info.format(displayMode, smallImageText)
        }

        val projectRepositoryUrl = info.projectRepositoryUrl
        if (projectRepositoryUrl != null) {
            it.addButton(ActivityButton("Repository", projectRepositoryUrl))
            it.activityButtonsMode = ActivityButtonsMode.BUTTONS
        }

        it.timestamps().start = info.start
    }
}