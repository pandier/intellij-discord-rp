package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings

@Suppress("MemberVisibilityCanBePrivate")
class ActivityInfo(
    val project: Project,
    val file: VirtualFile? = null,
    val app: ApplicationInfo = ApplicationInfo.getInstance(),
) {
    fun getActivityFactory(settings: DiscordSettings): ActivityFactory = when {
        file != null -> settings.fileActivityFactory
        else -> settings.projectActivityFactory
    }

    fun createActivity(settings: DiscordSettings): Activity =
        getActivityFactory(settings).create(this)

    fun format(string: String): String {
        val variables = mutableMapOf(
            "{app_name}" to app.fullApplicationName,
            "{project_name}" to project.name,
        )
        if (file != null) {
            variables.putAll(
                mapOf(
                    "{file_name}" to file.name,
                )
            )
        }
        var formatted = string
        variables.forEach { (variable, value) -> formatted = formatted.replace(variable, value) }
        return formatted
    }
}