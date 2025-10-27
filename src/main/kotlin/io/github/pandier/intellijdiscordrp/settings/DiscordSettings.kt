package io.github.pandier.intellijdiscordrp.settings

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.ActivityFactory
import io.github.pandier.intellijdiscordrp.settings.project.DiscordProjectSettings

data class DiscordSettings(
    var reconnectOnUpdate: Boolean = true,
    var customApplicationIdEnabled: Boolean = false,
    var customApplicationId: String = "",
    var defaultDisplayMode: ActivityDisplayMode = ActivityDisplayMode.FILE,
    var focusTimeoutEnabled: Boolean = true,
    var focusTimeoutMinutes: Int = 20,
    var logoStyle: LogoStyleSetting = LogoStyleSetting.MODERN,
    var showFullApplicationName: Boolean = false,
    var applicationMode: Mode = Mode(),
    var projectMode: Mode = Mode(
        details = "In {project_name}",
        timestampTarget = TimestampTargetSetting.PROJECT,
    ),
    var fileMode: Mode = Mode(
        details = "In {project_name}",
        state = "Editing {file_name}",
        largeIcon = Icon(
            type = IconType.FILE,
            tooltip = "{file_type}",
        ),
        smallIcon = Icon(
            type = IconType.APPLICATION,
            tooltip = "{app_name}",
        ),
        timestampTarget = TimestampTargetSetting.PROJECT,
    ),
) {
    data class Mode(
        var details: String = "",
        var state: String = "",
        var largeIcon: Icon = Icon(
            type = IconType.APPLICATION,
            tooltip = "{app_name}",
        ),
        var smallIcon: Icon = Icon(),
        var timestampEnabled: Boolean = true,
        var timestampTarget: TimestampTargetSetting = TimestampTargetSetting.APPLICATION,
    )

    data class Icon(
        var type: IconType = IconType.HIDDEN,
        var tooltip: String = "",
        var altType: IconType = IconType.HIDDEN,
        var altTooltip: String = "",
    )

    fun createActivityFactory(mode: ActivityDisplayMode, projectSettings: DiscordProjectSettings?): ActivityFactory {
        val projectSettings = projectSettings.takeIf { mode != ActivityDisplayMode.APPLICATION }
        val modeSettings = when (mode) {
            ActivityDisplayMode.APPLICATION -> applicationMode
            ActivityDisplayMode.PROJECT -> projectMode
            ActivityDisplayMode.FILE -> fileMode
        }
        return ActivityFactory(
            displayMode = mode,
            modeSettings = modeSettings,
            logoStyle = logoStyle,
            projectIcon = projectSettings?.icon,
            buttonText = if (projectSettings?.buttonEnabled == true) projectSettings.buttonText else null,
            buttonUrl = projectSettings?.buttonUrl ?: "",
        )
    }
}
