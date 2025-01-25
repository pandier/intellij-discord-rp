package io.github.pandier.intellijdiscordrp.activity

import io.github.pandier.intellijdiscordrp.util.git.git
import io.github.pandier.intellijdiscordrp.util.formatFileSize

/**
 * Represents an activity display mode.
 */
enum class ActivityDisplayMode(
    private val friendlyName: String,
    private val condition: ActivityContext.() -> Boolean = { true },
    val variables: List<ActivityVariable>,
) {
    /**
     * Shown only when deliberately configured in settings, mainly used for privacy reasons.
     */
    APPLICATION(
        friendlyName = "Application",
        variables = listOf(
            ActivityVariable(
                name = "app_name",
                description = "Name of the application",
                getter = { appName }
            ),
            ActivityVariable(
                name = "app_full_name",
                description = "Name and edition of the application",
                getter = { appFullName }
            ),
            ActivityVariable(
                name = "app_version",
                description = "Version of the application",
                getter = { appVersion }
            )
        )
    ),

    /**
     * Shown when not editing a file or configured in settings.
     */
    PROJECT(
        friendlyName = "Project",
        variables = APPLICATION.variables.plus(listOf(
            ActivityVariable(
                name = "project_name",
                description = "Name of the current project",
                getter = { projectName }
            ),
            ActivityVariable(
                name = "project_repo_url",
                description = "URL of the current project's git repository",
                availabilityCheck = { if (git != null) null else "Git plugin not installed" },
                getter = { projectRepositoryUrl ?: "-" }
            )
        ))
    ),

    /**
     * Shown when editing a file.
     */
    FILE(
        friendlyName = "File",
        condition = { file != null },
        variables = PROJECT.variables.plus(listOf(
            ActivityVariable(
                name = "file_name",
                description = "Name of the edited file",
                getter = { file?.name }
            ),
            ActivityVariable(
                name = "file_path",
                description = "Path of the edited file",
                getter = { file?.path }
            ),
            ActivityVariable(
                name = "file_type",
                description = "The determined type of the edited file",
                getter = { file?.typeName }
            ),
            ActivityVariable(
                name = "file_dir_name",
                description = "Name of the directory of the edited file",
                getter = { file?.directoryName }
            ),
            ActivityVariable(
                name = "file_line",
                description = "Line number of the current line in the edited file",
                getter = { file?.line?.toString() ?: "-" }
            ),
            ActivityVariable(
                name = "file_line_count",
                description = "Number of lines of the edited file",
                getter = { file?.lineCount?.toString() ?: "-" }
            ),
            ActivityVariable(
                name = "file_size",
                description = "Size of the edited file",
                getter = { file?.length?.let { formatFileSize(it) } ?: "-" }
            ),
        ))
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
        fun getSupportedFrom(highest: ActivityDisplayMode?, context: ActivityContext): ActivityDisplayMode {
            val values = values()
            for (i in (highest?.ordinal ?: values.lastIndex) downTo 0)
                if (values[i].supports(context))
                    return values[i]
            return APPLICATION
        }

        /**
         * Returns the highest display mode that supports the given [ActivityContext].
         */
        fun getSupported(context: ActivityContext): ActivityDisplayMode =
            getSupportedFrom(null, context)
    }

    /**
     * Returns true if this display mode supports the given [ActivityContext].
     */
    fun supports(context: ActivityContext): Boolean =
        condition(context)

    /**
     * Formats the given [string] with all the variables in this display mode.
     *
     * @see ActivityVariable.format
     */
    fun format(string: String, context: ActivityContext): String {
        var formatted = string
        variables.forEach { formatted = it.format(formatted, context) }
        return formatted
    }

    override fun toString(): String =
        friendlyName
}