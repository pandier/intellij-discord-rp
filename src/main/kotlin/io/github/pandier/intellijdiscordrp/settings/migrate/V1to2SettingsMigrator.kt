package io.github.pandier.intellijdiscordrp.settings.migrate

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import io.github.pandier.intellijdiscordrp.settings.IconType
import io.github.pandier.intellijdiscordrp.settings.LogoStyleSetting
import io.github.pandier.intellijdiscordrp.settings.TimestampTargetSetting

object V1to2SettingsMigrator : SettingsMigrator<V1to2SettingsMigrator.V1Model, DiscordSettings> {
    override val previousClass: Class<V1Model> = V1Model::class.java
    override val previousMigrator: SettingsMigrator<*, V1Model>? = null

    override fun migrate(previous: V1Model): DiscordSettings {
        val defaults = DiscordSettings()
        return DiscordSettings(
            reconnectOnUpdate = previous.reconnectOnUpdate,
            customApplicationIdEnabled = previous.customApplicationIdEnabled,
            customApplicationId = previous.customApplicationId,
            defaultDisplayMode = migrateDisplayMode(previous.defaultDisplayMode) ?: defaults.defaultDisplayMode,
            focusTimeoutEnabled = previous.focusTimeoutEnabled,
            focusTimeoutMinutes = previous.focusTimeoutMinutes,
            logoStyle = migrateLogoStyle(previous.logoStyle) ?: defaults.logoStyle,
            showFullApplicationName = previous.showFullApplicationName,
            applicationMode = DiscordSettings.Mode(
                details = previous.applicationDetails,
                state = previous.applicationState,
                largeIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.applicationLargeImageEnabled, previous.applicationLargeImage) ?: defaults.applicationMode.largeIcon.type,
                    tooltip = previous.applicationLargeImageText,
                ),
                smallIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.applicationSmallImageEnabled, previous.applicationSmallImage) ?: defaults.applicationMode.smallIcon.type,
                    tooltip = previous.applicationSmallImageText,
                ),
                timestampEnabled = previous.applicationTimestampEnabled,
                timestampTarget = TimestampTargetSetting.APPLICATION,
            ),
            projectMode = DiscordSettings.Mode(
                details = previous.projectDetails,
                state = previous.projectState,
                largeIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.projectLargeImageEnabled, previous.projectLargeImage) ?: defaults.projectMode.largeIcon.type,
                    tooltip = previous.projectLargeImageText,
                ),
                smallIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.projectSmallImageEnabled, previous.projectSmallImage) ?: defaults.projectMode.smallIcon.type,
                    tooltip = previous.projectSmallImageText,
                ),
                timestampEnabled = previous.projectTimestampEnabled,
                timestampTarget = migrateTimestampTarget(previous.projectTimestampTarget) ?: defaults.projectMode.timestampTarget,
            ),
            fileMode = DiscordSettings.Mode(
                details = previous.fileDetails,
                state = previous.fileState,
                largeIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.fileLargeImageEnabled, previous.fileLargeImage) ?: defaults.fileMode.largeIcon.type,
                    tooltip = previous.fileLargeImageText,
                ),
                smallIcon = DiscordSettings.Icon(
                    type = migrateIconType(previous.fileSmallImageEnabled, previous.fileSmallImage) ?: defaults.fileMode.smallIcon.type,
                    tooltip = previous.fileSmallImageText,
                ),
                timestampEnabled = previous.fileTimestampEnabled,
                timestampTarget = migrateTimestampTarget(previous.fileTimestampTarget) ?: defaults.fileMode.timestampTarget,
            ),
        )
    }

    private fun migrateIconType(enabled: Boolean, value: String): IconType? {
        if (!enabled) return IconType.HIDDEN
        return when (value.lowercase()) {
            "application" -> IconType.APPLICATION
            "file" -> IconType.FILE
            else -> null
        }
    }

    private fun migrateTimestampTarget(original: String): TimestampTargetSetting? {
        return when (original.lowercase()) {
            "application" -> TimestampTargetSetting.APPLICATION
            "project" -> TimestampTargetSetting.PROJECT
            "file" -> TimestampTargetSetting.FILE
            else -> null
        }
    }

    private fun migrateDisplayMode(original: String): ActivityDisplayMode? {
        return when (original.lowercase()) {
            "application" -> ActivityDisplayMode.APPLICATION
            "project" -> ActivityDisplayMode.PROJECT
            "file" -> ActivityDisplayMode.FILE
            else -> null
        }
    }

    private fun migrateLogoStyle(original: String): LogoStyleSetting? {
        return when (original.lowercase()) {
            "modern" -> LogoStyleSetting.MODERN
            "classic" -> LogoStyleSetting.CLASSIC
            else -> null
        }
    }

    data class V1Model(
        var reconnectOnUpdate: Boolean = true,
        var customApplicationIdEnabled: Boolean = false,
        var customApplicationId: String = "",
        var defaultDisplayMode: String = "File",
        var focusTimeoutEnabled: Boolean = true,
        var focusTimeoutMinutes: Int = 20,
        var logoStyle: String = "Modern",
        var showFullApplicationName: Boolean = false,

        var applicationDetails: String = "",
        var applicationState: String = "",
        var applicationLargeImage: String = "Application",
        var applicationLargeImageEnabled: Boolean = true,
        var applicationLargeImageText: String = "{app_name}",
        var applicationSmallImage: String = "Application",
        var applicationSmallImageEnabled: Boolean = false,
        var applicationSmallImageText: String = "",
        var applicationTimestampEnabled: Boolean = true,

        var projectDetails: String = "In {project_name}",
        var projectState: String = "",
        var projectLargeImage: String = "Application",
        var projectLargeImageEnabled: Boolean = true,
        var projectLargeImageText: String = "{app_name}",
        var projectSmallImage: String = "Application",
        var projectSmallImageEnabled: Boolean = false,
        var projectSmallImageText: String = "",
        var projectTimestampEnabled: Boolean = true,
        var projectTimestampTarget: String = "Project",

        var fileDetails: String = "In {project_name}",
        var fileState: String = "Editing {file_name}",
        var fileLargeImage: String = "File",
        var fileLargeImageEnabled: Boolean = true,
        var fileLargeImageText: String = "{file_type}",
        var fileSmallImage: String = "Application",
        var fileSmallImageEnabled: Boolean = true,
        var fileSmallImageText: String = "{app_name}",
        var fileTimestampEnabled: Boolean = true,
        var fileTimestampTarget: String = "Project",
    )
}