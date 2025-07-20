package io.github.pandier.intellijdiscordrp.settings

enum class IconType(
    private val friendlyName: String,
) {
    HIDDEN("Hidden"),
    APPLICATION("Application"),
    PROJECT("Project"),
    FILE("File");

    override fun toString(): String =
        friendlyName
}
