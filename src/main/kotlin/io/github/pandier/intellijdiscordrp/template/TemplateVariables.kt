package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.ActivityVariable
import io.github.pandier.intellijdiscordrp.util.formatFileSize
import io.github.pandier.intellijdiscordrp.util.git.git

@Suppress("unused")
object TemplateVariables {
    private val registry: MutableMap<String, ActivityVariable> = mutableMapOf()

    /*
     * APP
     */

    val appName = ActivityVariable(
        name = "app_name",
        description = "Name of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appName }
    ).register()

    val appFullName = ActivityVariable(
        name = "app_full_name",
        description = "Name and edition of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appFullName }
    ).register()

    val appVersion = ActivityVariable(
        name = "app_version",
        description = "Version of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appVersion }
    ).register()

    /*
     * PROJECT
     */

    val projectName = ActivityVariable(
        name = "project_name",
        displayMode = ActivityDisplayMode.PROJECT,
        description = "Name of the current project",
        getter = { projectName }
    ).register()

    val projectRepoUrl = ActivityVariable(
        name = "project_repo_url",
        description = "URL of the current project's Git repository remote",
        displayMode = ActivityDisplayMode.PROJECT,
        availabilityCheck = { if (git != null) null else "Git plugin not installed" },
        getter = { projectRepositoryUrl ?: "-" }
    ).register()

    val projectRepoBranch = ActivityVariable(
        name = "project_repo_branch",
        description = "Name of the current project's Git repository branch",
        displayMode = ActivityDisplayMode.PROJECT,
        availabilityCheck = { if (git != null) null else "Git plugin not installed" },
        getter = { projectRepositoryBranch ?: "-" }
    ).register()

    /*
     * FILE
     */

    val fileName = ActivityVariable(
        name = "file_name",
        description = "Name of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.name }
    ).register()

    val filePath = ActivityVariable(
        name = "file_path",
        description = "Path of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.path }
    ).register()

    val fileType = ActivityVariable(
        name = "file_type",
        description = "The determined type of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.typeName }
    ).register()

    val fileDirName = ActivityVariable(
        name = "file_dir_name",
        description = "Name of the directory of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.directoryName }
    ).register()

    val fileLine = ActivityVariable(
        name = "file_line",
        description = "Line number of the caret in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.line?.toString() ?: "-" }
    ).register()

    val fileLineCount = ActivityVariable(
        name = "file_line_count",
        description = "Number of lines of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.lineCount?.toString() ?: "-" }
    ).register()

    val fileColumn = ActivityVariable(
        name = "file_column",
        description = "Column number of the caret in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.column?.toString() ?: "-" }
    ).register()

    val fileProblemsTotal = ActivityVariable(
        name = "file_problems_total",
        description = "Total number of problems (warnings and errors) in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.total?.toString() }
    ).register()

    val fileProblemsError = ActivityVariable(
        name = "file_problems_errors",
        description = "Number of errors in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.errors?.toString() }
    ).register()

    val fileProblemsWarnings = ActivityVariable(
        name = "file_problems_warnings",
        description = "Number of warnings in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.warnings?.toString() }
    ).register()

    val fileSize = ActivityVariable(
        name = "file_size",
        description = "Size of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.length?.let { formatFileSize(it) } ?: "-" }
    ).register()


    fun getByName(name: String): ActivityVariable? {
        return registry[name.lowercase()]
    }

    fun getAll(): Collection<ActivityVariable> {
        return registry.values
    }

    private fun ActivityVariable.register(): ActivityVariable {
        registry[name.lowercase()] = this
        return this
    }
}