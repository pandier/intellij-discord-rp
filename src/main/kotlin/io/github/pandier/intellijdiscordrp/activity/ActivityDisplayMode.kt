package io.github.pandier.intellijdiscordrp.activity

enum class ActivityDisplayMode(
    private val friendlyName: String,
) {
    APPLICATION("Application"),
    PROJECT("Project"),
    FILE("File");

    fun getLower(other: ActivityDisplayMode) =
        if (other < this) other else this

    override fun toString(): String =
        friendlyName
}