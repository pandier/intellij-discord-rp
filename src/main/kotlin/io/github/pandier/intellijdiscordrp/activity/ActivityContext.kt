package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.service.timeTrackingService
import java.time.Instant

class ActivityFileContext(
    val name: String,
    val path: String,
    val type: ActivityFileType,
)

class ActivityContext(
    val start: Instant,
    val appName: String,
    val appFullName: String,
    val projectName: String,
    val file: ActivityFileContext? = null,
) {
    companion object Factory {
        fun create(
            project: Project,
            file: VirtualFile? = null,
            start: Instant = timeTrackingService.getOrTrack(project),
        ): ActivityContext {
            val app = ApplicationInfo.getInstance()
            return ActivityContext(
                start = start,
                appName = app.versionName,
                appFullName = app.fullApplicationName,
                projectName = project.name,
                file = file?.let {
                    ActivityFileContext(
                        name = it.name,
                        path = it.path,
                        type = it.fileType.activityFileType,
                    )
                }
            )
        }
    }

    val variables: Map<String, String>
        get() = buildMap {
            put("{app_name}", appName)
            put("{app_full_name}", appFullName)
            put("{project_name}", projectName)
            if (file != null) {
                put("{file_name}", file.name)
                put("{file_path}", file.path)
            }
        }

    fun format(string: String): String {
        var formatted = string
        variables.forEach { (variable, value) -> formatted = formatted.replace(variable, value) }
        return formatted
    }
}