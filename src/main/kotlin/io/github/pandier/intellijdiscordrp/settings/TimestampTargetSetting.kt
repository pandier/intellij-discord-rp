package io.github.pandier.intellijdiscordrp.settings

enum class TimestampTargetSetting(
    private val friendlyName: String
) {
    APPLICATION("Application"),
    PROJECT("Project"),
    FILE("File");

    override fun toString(): String =
        friendlyName
}