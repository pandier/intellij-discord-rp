package io.github.pandier.intellijdiscordrp.activity

/**
 * Represents an activity display mode.
 */
enum class ActivityDisplayMode(
    val friendlyName: String,
    val visibilityName: String,
    private val condition: ActivityContext.() -> Boolean = { true },
) {
    HIDDEN(
        friendlyName = "Hidden",
        visibilityName = "Hidden",
    ),

    /**
     * Shown only when deliberately configured in settings, mainly used for privacy reasons.
     */
    APPLICATION(
        friendlyName = "Application",
        visibilityName = "Application",
    ),

    /**
     * Shown when not editing a file or configured in settings.
     */
    PROJECT(
        friendlyName = "Project",
        visibilityName = "Project",
    ),

    /**
     * Shown when editing a file.
     */
    FILE(
        friendlyName = "File",
        visibilityName = "Project and Files",
        condition = { file != null },
    );

    companion object {

        /**
         * Returns the display mode with the given [name].
         */
        fun byName(name: String): ActivityDisplayMode? {
            return try {
                valueOf(name.uppercase())
            } catch (_: IllegalArgumentException) {
                null
            }
        }

        /**
         * Returns the highest display mode that isn't higher the given display mode
         * and supports the given [ActivityContext].
         *
         * If the given display mode is null, the highest display mode is used as starting point.
         */
        fun getSupportedFrom(highest: ActivityDisplayMode, context: ActivityContext): ActivityDisplayMode {
            val values = values()
            for (i in highest.ordinal downTo 0)
                if (values[i].supports(context))
                    return values[i]
            return APPLICATION
        }
    }

    /**
     * Returns true if this display mode supports the given [ActivityContext].
     */
    fun supports(context: ActivityContext): Boolean =
        condition(context)
}