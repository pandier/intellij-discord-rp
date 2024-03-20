package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import io.github.pandier.intellijdiscordrp.icon.IconType
import io.github.pandier.intellijdiscordrp.service.timeTrackingService

class ActivityFactory(
    private val iconTheme: IconTheme = DefaultIconTheme,
    private val details: String = "",
    private val state: String = "",
    private val largeImage: IconType? = null,
    private val largeImageText: String = "",
    private val smallImage: IconType? = null,
    private val smallImageText: String = "",
) {
    fun create(info: ActivityInfo): Activity = Activity().also {
        if (details.isNotEmpty())
            it.details = info.format(details)
        if (state.isNotEmpty())
            it.state = info.format(state)

        if (largeImage != null && largeImageText.isNotEmpty()) {
            it.assets().largeImage = iconTheme.getByType(largeImage, info)
            it.assets().largeText = info.format(largeImageText)
        }

        if (smallImage != null && smallImageText.isNotEmpty()) {
            it.assets().smallImage = iconTheme.getByType(smallImage, info)
            it.assets().smallText = info.format(smallImageText)
        }

        it.timestamps().start = timeTrackingService.getOrTrack(info.project)
    }
}