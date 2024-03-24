package io.github.pandier.intellijdiscordrp.settings

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityFactory

enum class IconTypeSetting(
    private val friendlyName: String
) {
    APPLICATION("Application"),
    FILE("File");

    override fun toString(): String =
        friendlyName
}

data class DiscordSettings(
    var reconnectOnUpdate: Boolean = true,
    var customApplicationIdEnabled: Boolean = false,
    var customApplicationId: String = "",

    var projectDetails: String = "In {project_name}",
    var projectState: String = "",
    var projectLargeImage: IconTypeSetting = IconTypeSetting.APPLICATION,
    var projectLargeImageEnabled: Boolean = true,
    var projectLargeImageText: String = "{app_name}",
    var projectSmallImage: IconTypeSetting = IconTypeSetting.APPLICATION,
    var projectSmallImageEnabled: Boolean = false,
    var projectSmallImageText: String = "",

    var fileDetails: String = "In {project_name}",
    var fileState: String = "Editing {file_name}",
    var fileLargeImage: IconTypeSetting = IconTypeSetting.FILE,
    var fileLargeImageEnabled: Boolean = true,
    var fileLargeImageText: String = "{file_type}",
    var fileSmallImage: IconTypeSetting = IconTypeSetting.APPLICATION,
    var fileSmallImageEnabled: Boolean = true,
    var fileSmallImageText: String = "{app_name}",
) {
    val projectActivityFactory: ActivityFactory
        get() = ActivityFactory(
            details = projectDetails,
            state = projectState,
            largeImage = if (projectLargeImageEnabled) projectLargeImage else null,
            largeImageText = projectLargeImageText,
            smallImage = if (projectSmallImageEnabled) projectSmallImage else null,
            smallImageText = projectSmallImageText,
        )

    val fileActivityFactory: ActivityFactory
        get() = ActivityFactory(
            details = fileDetails,
            state = fileState,
            largeImage = if (fileLargeImageEnabled) fileLargeImage else null,
            largeImageText = fileLargeImageText,
            smallImage = if (fileSmallImageEnabled) fileSmallImage else null,
            smallImageText = fileSmallImageText,
        )

    fun getActivityFactory(context: ActivityContext) = when {
        context.file != null -> fileActivityFactory
        else -> projectActivityFactory
    }
}
