package io.github.pandier.intellijdiscordrp.settings

data class DiscordSettings(
    var projectDetails: String = "{project}",
    var projectState: String = "",
    var fileDetails: String = "{project}",
    var fileState: String = "Editing {file_name}",
)
