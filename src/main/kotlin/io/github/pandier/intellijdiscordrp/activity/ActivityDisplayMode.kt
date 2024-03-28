package io.github.pandier.intellijdiscordrp.activity

enum class ActivityDisplayMode {
    APPLICATION,
    PROJECT,
    FILE;

    fun getLower(other: ActivityDisplayMode) =
        if (other < this) other else this
}