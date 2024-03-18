package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import io.github.pandier.intellijdiscordrp.service.timeTrackingService

class ActivityFactory(
    private val iconTheme: IconTheme = DefaultIconTheme,
    private val details: String = "",
    private val state: String = "",
    private val largeImage: String = "",
    private val largeImageText: String = "",
    private val smallImage: String = "",
    private val smallImageText: String = "",
) {
    fun create(info: ActivityInfo): Activity = Activity().also {
        if (details.isNotEmpty())
            it.details = info.format(details, iconTheme)
        if (state.isNotEmpty())
            it.state = info.format(state, iconTheme)

        if (largeImage.isNotEmpty() && largeImageText.isNotEmpty()) {
            it.assets().largeImage = info.format(largeImage, iconTheme)
            it.assets().largeText = info.format(largeImageText, iconTheme)
        }

        if (smallImage.isNotEmpty() && smallImageText.isNotEmpty()) {
            it.assets().smallImage = info.format(smallImage, iconTheme)
            it.assets().smallText = info.format(smallImageText, iconTheme)
        }

        it.timestamps().start = timeTrackingService.getOrTrack(info.project)
    }
}