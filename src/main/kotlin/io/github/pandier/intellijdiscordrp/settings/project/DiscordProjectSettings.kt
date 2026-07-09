package io.github.pandier.intellijdiscordrp.settings.project

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.template.Templates

data class DiscordProjectSettings(
    @Deprecated(
        message = "Replaced by displayMode, kept for backwards compatibility",
        replaceWith = ReplaceWith("displayMode"),
    )
    var showRichPresence: Boolean = true,
    private var _displayMode: ActivityDisplayMode? = null,
    var icon: String = "",
    var buttonEnabled: Boolean = false,
    var buttonText: String = "View Repository",
    var buttonUrl: String = "{project_repo_url}",
) {
    @Suppress("DEPRECATION")
    var displayMode: ActivityDisplayMode?
        get() {
            // backwards compatibility
            if (!showRichPresence) return ActivityDisplayMode.HIDDEN
            return _displayMode
        }
        set(value) {
            // backwards compatibility
            showRichPresence = value != ActivityDisplayMode.HIDDEN
            _displayMode = value
        }

    val templates = Templates()

    val buttonTextNode by templates.add { buttonText }
    val buttonUrlNode by templates.add { buttonUrl }
}