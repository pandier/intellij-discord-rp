package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityInfo
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent

val discordService: DiscordService
    get() = service()

@Service
class DiscordService : Disposable {
    private var core: Core? = runCatching {
        Core(
            CreateParams().apply {
                clientID = 1107202385799041054L
                flags = CreateParams.getDefaultFlags()
            }
        )
    }.getOrElse {
        DiscordRichPresencePlugin.logger.info(
            "Ignoring rich presence, because Discord SDK could not be initialized",
            it
        )
        null
    }

    private var activityInfo: ActivityInfo? = null

    private fun accessInternal(block: (Core) -> Unit) {
        if (core?.isOpen == false) {
            DiscordRichPresencePlugin.logger.info("Ignoring rich presence, because Discord SDK was disconnected")
            core = null
        }

        try {
            core?.let(block)
        } catch (ex: RuntimeException) {
            DiscordRichPresencePlugin.logger.info(
                "Ignoring rich presence, because Discord SDK could not sent activity",
                ex
            )
            core = null
        }
    }

    fun changeActivity(project: Project, file: VirtualFile? = null) =
        changeActivity(ActivityInfo(project, file))

    @Suppress("MemberVisibilityCanBePrivate")
    fun changeActivity(activityInfo: ActivityInfo?) {
        this.activityInfo = activityInfo
        val activity = activityInfo?.createActivity(discordSettingsComponent.state)
        accessInternal { it.activityManager()?.updateActivity(activity) }
    }

    fun clearActivity() =
        changeActivity(null)

    fun updateActivity() =
        changeActivity(activityInfo)

    override fun dispose() {
        core?.close()
    }
}