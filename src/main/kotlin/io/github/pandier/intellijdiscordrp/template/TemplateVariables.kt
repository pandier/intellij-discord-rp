package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.util.formatFileSize
import io.github.pandier.intellijdiscordrp.util.git.git

@Suppress("unused")
object TemplateVariables {
    private val registry: MutableMap<String, TemplateVariable> = mutableMapOf()

    /*
     * APP
     */

    val appName = TemplateVariable(
        name = "app_name",
        description = "Name of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appName }
    ).register()

    val appFullName = TemplateVariable(
        name = "app_full_name",
        description = "Name and edition of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appFullName }
    ).register()

    val appVersion = TemplateVariable(
        name = "app_version",
        description = "Version of the application",
        displayMode = ActivityDisplayMode.APPLICATION,
        getter = { appVersion }
    ).register()

    /*
     * PROJECT
     */

    val projectName = TemplateVariable(
        name = "project_name",
        displayMode = ActivityDisplayMode.PROJECT,
        description = "Name of the current project",
        getter = { projectName }
    ).register()

    val projectRepoUrl = TemplateVariable(
        name = "project_repo_url",
        description = "URL of the current project's Git repository remote",
        displayMode = ActivityDisplayMode.PROJECT,
        availabilityCheck = { if (git != null) null else "Git plugin not installed" },
        getter = { projectRepositoryUrl ?: "-" }
    ).register()

    val projectRepoBranch = TemplateVariable(
        name = "project_repo_branch",
        description = "Name of the current project's Git repository branch",
        displayMode = ActivityDisplayMode.PROJECT,
        availabilityCheck = { if (git != null) null else "Git plugin not installed" },
        getter = { projectRepositoryBranch ?: "-" }
    ).register()

    /*
     * FILE
     */

    val fileName = TemplateVariable(
        name = "file_name",
        description = "Name of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.name }
    ).register()

    val filePath = TemplateVariable(
        name = "file_path",
        description = "Path of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.path }
    ).register()

    val fileType = TemplateVariable(
        name = "file_type",
        description = "The determined type of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.typeName }
    ).register()

    val fileDirName = TemplateVariable(
        name = "file_dir_name",
        description = "Name of the directory of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.directoryName }
    ).register()

    val fileLine = TemplateVariable(
        name = "file_line",
        description = "Line number of the caret in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.line?.toString() ?: "-" }
    ).register()

    val fileLineCount = TemplateVariable(
        name = "file_line_count",
        description = "Number of lines of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.lineCount?.toString() ?: "-" }
    ).register()

    val fileColumn = TemplateVariable(
        name = "file_column",
        description = "Column number of the caret in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.column?.toString() ?: "-" }
    ).register()

    val fileProblemsTotal = TemplateVariable(
        name = "file_problems_total",
        description = "Total number of problems (warnings and errors) in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.total?.toString() }
    ).register()

    val fileProblemsError = TemplateVariable(
        name = "file_problems_errors",
        description = "Number of errors in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.errors?.toString() }
    ).register()

    val fileProblemsWarnings = TemplateVariable(
        name = "file_problems_warnings",
        description = "Number of warnings in the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.problemCount?.warnings?.toString() }
    ).register()

    val fileSize = TemplateVariable(
        name = "file_size",
        description = "Size of the edited file",
        displayMode = ActivityDisplayMode.FILE,
        getter = { file?.length?.let { formatFileSize(it) } ?: "-" }
    ).register()


    fun getByName(name: String): TemplateVariable? {
        return registry[name.lowercase()]
    }

    fun getAll(): Collection<TemplateVariable> {
        return registry.values
    }

    private fun TemplateVariable.register(): TemplateVariable {
        registry[name.lowercase()] = this
        return this
    }
}