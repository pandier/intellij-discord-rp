package io.github.pandier.intellijdiscordrp.settings

import io.github.pandier.intellijdiscordrp.activity.ActivityFactory
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import io.github.pandier.intellijdiscordrp.icon.IconType

data class DiscordSettings(
    var projectDetails: String = "{project_name}",
    var projectState: String = "",
    var projectLargeImage: IconType = IconType.APPLICATION,
    var projectLargeImageText: String = "{app_name}",
    var projectSmallImage: IconType = IconType.APPLICATION,
    var projectSmallImageEnabled: Boolean = false,
    var projectSmallImageText: String = "",
    var fileDetails: String = "{project_name}",
    var fileState: String = "Editing {file_name}",
    var fileLargeImage: IconType = IconType.FILE,
    var fileLargeImageText: String = "{file_name}",
    var fileSmallImage: IconType = IconType.APPLICATION,
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
            if (projectSmallImageEnabled) projectSmallImage else null,
            if (projectSmallImageEnabled) projectSmallImageText else ""
        )

    val fileActivityFactory: ActivityFactory
        get() = ActivityFactory(
            DefaultIconTheme,
            fileDetails,
            fileState,
            fileLargeImage,
            fileLargeImageText,
            if (fileSmallImageEnabled) fileSmallImage else null,
            if (fileSmallImageEnabled) fileSmallImageText else ""
        )
}
