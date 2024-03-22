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

private fun connect(): Core? = runCatching {
    Core(
        CreateParams().apply {
            clientID = 1107202385799041054L
            flags = CreateParams.getDefaultFlags()
        }
    ).also {
        DiscordRichPresencePlugin.logger.info("Connected to Discord Client")
    }
}.getOrElse {
    DiscordRichPresencePlugin.logger.debug("Failed to connect to Discord Client", it)
    null
}

@Service
@Suppress("MemberVisibilityCanBePrivate")
class DiscordService : Disposable {
    private var internal: Core? = connect()
    private var activityInfo: ActivityInfo? = null

    private fun accessInternal(block: (Core) -> Unit) {
        if (internal == null && discordSettingsComponent.state.reconnectOnUpdate)
            reconnect()

        try {
            internal?.also(block)
        } catch (ex: RuntimeException) {
            internal?.close()
            internal = null

            DiscordRichPresencePlugin.logger.info(
                "Disconnected from Discord Client",
                ex
            )
        }
    }

    fun reconnect() {
        internal?.close()
        internal = connect()
    }

    fun changeActivity(project: Project, file: VirtualFile? = null) =
        changeActivity(
            ActivityInfo.from(
                start = timeTrackingService.getOrTrack(project),
                project = project,
                file = file,
            )
        )

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
        internal?.close()
    }
}