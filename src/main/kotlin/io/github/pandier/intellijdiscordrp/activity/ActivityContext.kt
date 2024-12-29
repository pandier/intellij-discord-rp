package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.service.TimeTrackingService
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.settings.project.DiscordProjectSettings
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent
import io.github.vyfor.kpresence.rpc.Activity
import java.lang.ref.WeakReference
import java.time.Instant
import kotlin.math.max

class ActivityFileContext(
    val name: String,
    val path: String,
    val directoryName: String,
    val type: ActivityFileType,
    val typeName: String,
    val line: Int?,
    val lineCount: Int?,
    val length: Long?,
    val start: Instant,
)

class ActivityContext(
    val appName: String,
    val appFullName: String,
    val appVersion: String,
    val appStart: Instant,
    val project: WeakReference<Project>,
    val projectName: String,
    val projectStart: Instant,
    val file: ActivityFileContext? = null,
) {
    companion object Factory {
        fun create(
            project: Project,
            file: VirtualFile? = null,
            editor: Editor? = null
        ): ActivityContext {
            val timeTrackingService = TimeTrackingService.getInstance()
            val appInfo = ApplicationInfo.getInstance()
            val appNames = ApplicationNamesInfo.getInstance()
            return ActivityContext(
                appName = appNames.fullProductName,
                appFullName = appNames.fullProductNameWithEdition,
                appVersion = appInfo.fullVersion,
                appStart = timeTrackingService.getOrInit(ApplicationManager.getApplication()),
                project = WeakReference(project),
                projectName = project.name,
                projectStart = timeTrackingService.getOrInit(project),
                file = file?.let {
                    val contentRoot = ReadAction.compute<VirtualFile?, Exception> {
                        ProjectFileIndex.getInstance(project).getContentRootForFile(it)
                    }
                    val relativePath = contentRoot?.let { i -> VfsUtil.getRelativePath(it, i) } ?: it.name
                    val activityFileType = it.activityFileType
                    ActivityFileContext(
                        name = activityFileType?.replaceFileName ?: it.name,
                        path = activityFileType?.replaceFileName ?: relativePath,
                        directoryName = it.parent?.name ?: "",
                        type = activityFileType ?: ActivityFileType.OTHER,
                        typeName = activityFileType?.friendlyName ?: it.fileType.name,
                        line = editor?.caretModel?.logicalPosition?.line?.plus(1),
                        lineCount = editor?.document?.lineCount?.let { max(it, 1) },
                        length = it.length,
                        start = timeTrackingService.getOrInit(it),
                    )
                }
            )
        }
    }

    /**
     * Renders this activity context to an [Activity] using global plugin settings
     * and plugin settings of the project with respect to settings hiding the activity.
     * Returns null if the reference to the projet was already dropped.
     */
    fun createActivity(): Activity? {
        return createActivity(
            discordSettingsComponent.state,
            project.get()?.discordProjectSettingsComponent?.state
        )
    }

    /**
     * Renders this activity context to an [Activity] using the given [settings]
     * and the display mode of the given [projectSettings] with respect to settings hiding the activity.
     *
     * Returns null if [projectSettings] is null.
     */
    fun createActivity(
        settings: DiscordSettings,
        projectSettings: DiscordProjectSettings?,
    ): Activity? {
        if (projectSettings == null || !projectSettings.showRichPresence)
            return null
        val displayMode = ActivityDisplayMode.getSupportedFrom(
            projectSettings.displayMode ?: settings.defaultDisplayMode,
            this
        )
        return settings.getActivityFactory(displayMode).create(this)
    }
}