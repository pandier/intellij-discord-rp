package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import java.time.Instant

class ActivityFileInfo(
    val name: String,
    val path: String,
    val type: String,
)

@Suppress("MemberVisibilityCanBePrivate")
class ActivityInfo(
    val start: Instant,
    val appName: String,
    val appFullName: String,
    val appProductCode: String,
    val projectName: String,
    val file: ActivityFileInfo? = null,
) {
    companion object {
        fun from(
            start: Instant,
            project: Project,
            file: VirtualFile? = null,
            app: ApplicationInfo = ApplicationInfo.getInstance(),
        ) = ActivityInfo(
            start = start,
            appName = app.versionName,
            appFullName = app.fullApplicationName,
            appProductCode = app.build.productCode,
            projectName = project.name,
            file = file?.let {
                ActivityFileInfo(
                    name = it.name,
                    path = it.path,
                    type = it.fileType.name,
                )
            }
        )
    }

    fun getActivityFactory(settings: DiscordSettings): ActivityFactory = when {
        file != null -> settings.fileActivityFactory
        else -> settings.projectActivityFactory
    }

    fun createActivity(settings: DiscordSettings): Activity =
        getActivityFactory(settings).create(this)

    val variables
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