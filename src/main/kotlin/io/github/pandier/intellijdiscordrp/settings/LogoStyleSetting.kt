package io.github.pandier.intellijdiscordrp.settings

enum class LogoStyleSetting(
    private val friendlyName: String
) {
    MODERN("Modern"),
    CLASSIC("Classic");

    override fun toString(): String =
        friendlyName
}