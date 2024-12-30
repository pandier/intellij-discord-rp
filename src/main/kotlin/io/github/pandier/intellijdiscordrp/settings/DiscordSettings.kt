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

    var applicationDetails: String = "",
    var applicationState: String = "",
    var applicationLargeImage: ImageSetting = ImageSetting.APPLICATION,
    var applicationLargeImageEnabled: Boolean = true,
    var applicationLargeImageText: String = "{app_name}",
    var applicationSmallImage: ImageSetting = ImageSetting.APPLICATION,
    var applicationSmallImageEnabled: Boolean = false,
    var applicationSmallImageText: String = "",
    var applicationTimestampEnabled: Boolean = true,

    var projectDetails: String = "In {project_name}",
    var projectState: String = "",
    var projectLargeImage: ImageSetting = ImageSetting.APPLICATION,
    var projectLargeImageEnabled: Boolean = true,
    var projectLargeImageText: String = "{app_name}",
    var projectSmallImage: ImageSetting = ImageSetting.APPLICATION,
    var projectSmallImageEnabled: Boolean = false,
    var projectSmallImageText: String = "",
    var projectRepoButtonEnabled: Boolean = true,
    var projectRepoButtonText: String = "View Repository",
    var projectTimestampEnabled: Boolean = true,
    var projectTimestampTarget: TimestampTargetSetting = TimestampTargetSetting.PROJECT,

    var fileDetails: String = "In {project_name}",
    var fileState: String = "Editing {file_name}",
    var fileLargeImage: ImageSetting = ImageSetting.FILE,
    var fileLargeImageEnabled: Boolean = true,
    var fileLargeImageText: String = "{file_type}",
    var fileSmallImage: ImageSetting = ImageSetting.APPLICATION,
    var fileSmallImageEnabled: Boolean = true,
    var fileSmallImageText: String = "{app_name}",
    var fileRepoButtonEnabled: Boolean = true,
    var fileRepoButtonText: String = "View Repository",
    var fileTimestampEnabled: Boolean = true,
    var fileTimestampTarget: TimestampTargetSetting = TimestampTargetSetting.PROJECT,
) {
    fun createApplicationActivityFactory(): ActivityFactory = ActivityFactory(
        displayMode = ActivityDisplayMode.APPLICATION,
        logoStyle = logoStyle,
        details = applicationDetails,
        state = applicationState,
        largeImage = if (applicationLargeImageEnabled) applicationLargeImage else null,
        largeImageText = applicationLargeImageText,
        smallImage = if (applicationSmallImageEnabled) applicationSmallImage else null,
        smallImageText = applicationSmallImageText,
        repoButtonText = null,
        timestampEnabled = applicationTimestampEnabled,
        timestampTarget = TimestampTargetSetting.APPLICATION,
    )

    fun createProjectActivityFactory(projectSettings: DiscordProjectSettings?): ActivityFactory = ActivityFactory(
        displayMode = ActivityDisplayMode.PROJECT,
        logoStyle = logoStyle,
        details = projectDetails,
        state = projectState,
        largeImage = if (projectLargeImageEnabled) projectLargeImage else null,
        largeImageText = projectLargeImageText,
        smallImage = if (projectSmallImageEnabled) projectSmallImage else null,
        smallImageText = projectSmallImageText,
        repoButtonText = if (projectRepoButtonEnabled && projectSettings?.showRepoButton != false) projectRepoButtonText else null,
        timestampEnabled = projectTimestampEnabled,
        timestampTarget = projectTimestampTarget,
    )

    fun createFileActivityFactory(projectSettings: DiscordProjectSettings?): ActivityFactory = ActivityFactory(
        displayMode = ActivityDisplayMode.FILE,
        logoStyle = logoStyle,
        details = fileDetails,
        state = fileState,
        largeImage = if (fileLargeImageEnabled) fileLargeImage else null,
        largeImageText = fileLargeImageText,
        smallImage = if (fileSmallImageEnabled) fileSmallImage else null,
        smallImageText = fileSmallImageText,
        repoButtonText = if (fileRepoButtonEnabled && projectSettings?.showRepoButton != false) fileRepoButtonText else null,
        timestampEnabled = fileTimestampEnabled,
        timestampTarget = fileTimestampTarget,
    )

    fun createActivityFactory(mode: ActivityDisplayMode, projectSettings: DiscordProjectSettings?) = when (mode) {
        ActivityDisplayMode.APPLICATION -> createApplicationActivityFactory()
        ActivityDisplayMode.PROJECT -> createProjectActivityFactory(projectSettings)
        ActivityDisplayMode.FILE -> createFileActivityFactory(projectSettings)
    }
}
