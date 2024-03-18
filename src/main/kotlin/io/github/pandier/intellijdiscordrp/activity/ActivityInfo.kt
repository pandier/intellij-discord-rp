package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import java.time.Instant

@Suppress("MemberVisibilityCanBePrivate")
class ActivityInfo(
    val project: Project,
    val file: VirtualFile? = null,
    val app: ApplicationInfo = ApplicationInfo.getInstance(),
    val start: Instant = Instant.now(),
) {
    fun getActivityFactory(settings: DiscordSettings): ActivityFactory = when {
        file != null -> settings.fileActivityFactory
        else -> settings.projectActivityFactory
    }

    fun createActivity(settings: DiscordSettings): Activity =
        getActivityFactory(settings).create(this)

    fun format(string: String, iconTheme: IconTheme): String {
        val variables = mutableMapOf(
            "{app_name}" to app.fullApplicationName,
            "{app_icon}" to iconTheme.getPlatform(app),
            "{project_name}" to project.name,
        )
        if (file != null) {
            variables.putAll(
                mapOf(
                    "{file_name}" to file.name,
                    "{file_icon}" to iconTheme.getFile(file.fileType)
                )
            )
        }
        var formatted = string
        variables.forEach { (variable, value) -> formatted = formatted.replace(variable, value) }
        return formatted
    }
}