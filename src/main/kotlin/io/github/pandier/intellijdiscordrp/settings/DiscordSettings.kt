package io.github.pandier.intellijdiscordrp.settings

import io.github.pandier.intellijdiscordrp.activity.ActivityFactory
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme

data class DiscordSettings(
    var projectDetails: String = "{project_name}",
    var projectState: String = "",
    var projectLargeImage: String = "{app_icon}",
    var projectLargeImageText: String = "{app_name}",
    var projectSmallImage: String = "",
    var projectSmallImageEnabled: Boolean = false,
    var projectSmallImageText: String = "",
    var fileDetails: String = "{project_name}",
    var fileState: String = "Editing {file_name}",
    var fileLargeImage: String = "{file_icon}",
    var fileLargeImageText: String = "{file_name}",
    var fileSmallImage: String = "{app_icon}",
    var fileSmallImageEnabled: Boolean = true,
    var fileSmallImageText: String = "{app_name}",
) {
    val projectActivityFactory: ActivityFactory
        get() = ActivityFactory(
            DefaultIconTheme,
            projectDetails,
            projectState,
            projectLargeImage,
            projectLargeImageText,
            if (projectSmallImageEnabled) projectSmallImage else "",
            if (projectSmallImageEnabled) projectSmallImageText else ""
        )

    val fileActivityFactory: ActivityFactory
        get() = ActivityFactory(
            DefaultIconTheme,
            fileDetails,
            fileState,
            fileLargeImage,
            fileLargeImageText,
            if (fileSmallImageEnabled) fileSmallImage else "",
            if (fileSmallImageEnabled) fileSmallImageText else ""
        )
}
