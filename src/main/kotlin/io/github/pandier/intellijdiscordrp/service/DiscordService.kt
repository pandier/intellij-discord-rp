package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent

val discordService: DiscordService
    get() = service()

private fun connect(): Core? = runCatching {
    Core(
        CreateParams().apply {
            clientID = currentActivityApplicationType.discordApplicationId
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
class DiscordService : Disposable {
    private var internal: Core? = connect()
    private var activityContext: ActivityContext? = null

    private fun accessInternal(block: (Core) -> Unit) {
        if (internal == null && discordSettingsComponent.state.reconnectOnUpdate)
            reconnect(false)

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

    fun reconnect(update: Boolean = true) {
        internal?.close()
        internal = connect()

        if (update)
            updateActivity()
    }

    fun changeActivity(activityContext: ActivityContext?) {
        this.activityContext = activityContext
        val activityFactory = activityContext?.let(discordSettingsComponent.state::getActivityFactory)
        val activity = activityFactory?.create(activityContext)
        accessInternal {
            it.activityManager()?.updateActivity(activity)
        }
    }

    fun clearActivity() =
        changeActivity(null)

    fun updateActivity() =
        changeActivity(activityContext)

    override fun dispose() {
        internal?.close()
    }
}