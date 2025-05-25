package io.github.pandier.intellijdiscordrp.settings.project

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

data class DiscordProjectSettings(
    var showRichPresence: Boolean = true,
    var displayMode: ActivityDisplayMode? = null,
    var icon: String = "",
    var buttonEnabled: Boolean = false,
    var buttonText: String = "View Repository",
    var buttonUrl: String = "{project_repo_url}",
)