package io.github.pandier.intellijdiscordrp.icon

enum class IconType(
    private val friendlyName: String
) {
    APPLICATION("Application"),
    FILE("File");

    override fun toString(): String =
        friendlyName
}