package io.github.pandier.intellijdiscordrp.settings

enum class ImageSetting(
    private val friendlyName: String
) {
    APPLICATION("Application"),
    FILE("File");

    override fun toString(): String =
        friendlyName
}
