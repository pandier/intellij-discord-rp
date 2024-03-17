package io.github.pandier.intellijdiscordrp.settings

data class DiscordSettings(
    var projectDetails: String = "{project}",
    var projectState: String = "",
    var projectLargeImage: String = "{app_icon}",
    var projectLargeImageText: String = "{app_name}",
    var projectSmallImage: String = "",
    var projectSmallImageEnabled: Boolean = false,
    var projectSmallImageText: String = "",
    var fileDetails: String = "{project}",
    var fileState: String = "Editing {file_name}",
    var fileLargeImage: String = "{file_icon}",
    var fileLargeImageText: String = "{file_name}",
    var fileSmallImage: String = "{app_icon}",
    var fileSmallImageEnabled: Boolean = true,
    var fileSmallImageText: String = "{app_name}",
)
