package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.service.TimeTrackingService
import java.lang.ref.WeakReference
import java.time.Instant

class ActivityFileContext(
    val name: String,
    val path: String,
    val type: ActivityFileType,
    val typeName: String,
)

class ActivityContext(
    val start: Instant,
    val appName: String,
    val appFullName: String,
    val project: WeakReference<Project>,
    val projectName: String,
    val file: ActivityFileContext? = null,
) {
    companion object Factory {
        fun create(
            project: Project,
            file: VirtualFile? = null,
            start: Instant = TimeTrackingService.getInstance(project).start,
        ): ActivityContext {
            val app = ApplicationInfo.getInstance()
            return ActivityContext(
                start = start,
                appName = app.versionName,
                appFullName = app.fullApplicationName,
                projectName = project.name,
                project = WeakReference(project),
                file = file?.let {
                    val activityFileType = it.activityFileType
                    ActivityFileContext(
                        name = it.name,
                        path = it.path,
                        type = activityFileType,
                        typeName = when (activityFileType) {
                            ActivityFileType.OTHER -> it.fileType.name
                            else -> activityFileType.friendlyName
                        },
                    )
                }
            )
        }
    }
}