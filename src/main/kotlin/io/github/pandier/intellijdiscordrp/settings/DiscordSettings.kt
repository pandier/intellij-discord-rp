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
        largeIcon = IconSetting.FILE,
        largeIconTooltip = "{file_type}",
        smallIcon = IconSetting.APPLICATION,
        smallIconTooltip = "{app_name}",
        timestampTarget = TimestampTargetSetting.FILE,
    ),
) {
    fun createActivityFactory(mode: ActivityDisplayMode, projectSettings: DiscordProjectSettings?): ActivityFactory {
        val projectSettings = projectSettings.takeIf { mode != ActivityDisplayMode.APPLICATION }
        val modeSettings = when (mode) {
            ActivityDisplayMode.APPLICATION -> applicationMode
            ActivityDisplayMode.PROJECT -> projectMode
            ActivityDisplayMode.FILE -> fileMode
        }
        return ActivityFactory(
            displayMode = mode,
            logoStyle = logoStyle,
            projectIcon = projectSettings?.icon,
            details = modeSettings.details,
            state = modeSettings.state,
            largeIcon = modeSettings.largeIcon,
            largeIconTooltip = modeSettings.largeIconTooltip,
            largeIconAlt = modeSettings.largeIconAlt,
            largeIconAltTooltip = modeSettings.largeIconAltTooltip,
            smallIcon = modeSettings.smallIcon,
            smallIconTooltip = modeSettings.smallIconTooltip,
            smallIconAlt = modeSettings.smallIconAlt,
            smallIconAltTooltip = modeSettings.smallIconAltTooltip,
            buttonText = if (projectSettings?.buttonEnabled == true) projectSettings.buttonText else null,
            buttonUrl = projectSettings?.buttonUrl ?: "",
            timestampEnabled = modeSettings.timestampEnabled,
            timestampTarget = modeSettings.timestampTarget,
        )
    }

    data class Mode(
        var details: String = "",
        var state: String = "",
        var largeIcon: IconSetting = IconSetting.APPLICATION,
        var largeIconTooltip: String = "{app_name}",
        var largeIconAlt: IconSetting = IconSetting.APPLICATION,
        var largeIconAltTooltip: String = "{app_name}",
        var smallIcon: IconSetting = IconSetting.HIDDEN,
        var smallIconTooltip: String = "",
        var smallIconAlt: IconSetting = IconSetting.APPLICATION,
        var smallIconAltTooltip: String = "{app_name}",
        var timestampEnabled: Boolean = true,
        var timestampTarget: TimestampTargetSetting = TimestampTargetSetting.APPLICATION,
    )
}
