package io.github.pandier.intellijdiscordrp.settings.project

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

data class DiscordProjectSettings(
    var showRichPresence: Boolean = true,
    var showRepoButton: Boolean = true,
    var displayMode: ActivityDisplayMode? = null,
    var buttonEnabled: Boolean = false,
    var buttonText: String = "",
    var buttonUrl: String = "",
)