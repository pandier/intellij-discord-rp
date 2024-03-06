package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.RichPresence
import io.github.pandier.intellijdiscordrp.activity.ActivityFactory
import io.github.pandier.intellijdiscordrp.activity.ActivityFactoryBuilder

val discordService: DiscordService
    get() = service()

@Service
class DiscordService : Disposable {
    private val core: Core? = runCatching {
        Core(
            CreateParams().apply {
                clientID = 1107202385799041054L
                flags = CreateParams.getDefaultFlags()
            }
        )
    }.getOrElse {
        DiscordRichPresencePlugin.logger.error("Failed to initialize rich presence", it)
        null
    }

    fun changeActivity(project: Project, block: ActivityFactoryBuilder.() -> Unit = {}) =
        changeActivity(ActivityFactoryBuilder(project).apply(block).build())

    fun changeActivity(activityFactory: ActivityFactory) =
        changeActivity(activityFactory.create())

    fun changeActivity(activity: Activity?) {
        if (activity != null) {
            core?.activityManager()?.updateActivity(activity)
        } else {
            clearActivity()
        }
    }

    fun clearActivity() =
        core?.activityManager()?.clearActivity()

    override fun dispose() {
        core?.close()
    }
}