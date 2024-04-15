package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.listener.RichPresenceFocusChangeListener
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.util.MergingRunner
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.channels.ClosedChannelException

private fun connect(): Core? = runCatching {
    val settings = discordSettingsComponent.state
    val applicationId = if (settings.customApplicationIdEnabled) {
        settings.customApplicationId.toULong().toLong()
    } else {
        currentActivityApplicationType.discordApplicationId
    }

    val internal = Core(CreateParams().apply {
        clientID = applicationId
        flags = CreateParams.getDefaultFlags()
    })

    DiscordRichPresencePlugin.logger.info("Connected to Discord Client")
    internal
}.getOrElse {
    DiscordRichPresencePlugin.logger.debug("Failed to connect to Discord Client", it)
    null
}

/**
 * A service that handles a connection with the Discord client and manages Rich Presence activities.
 */
@Service
class DiscordService(
    val scope: CoroutineScope,
) : Disposable {
    companion object {
        @JvmStatic
        fun getInstance(): DiscordService = service()
    }

    init {
        // Register focus change listener
        val eventMulticaster = EditorFactory.getInstance().eventMulticaster
        val eventMulticasterEx = eventMulticaster as? EditorEventMulticasterEx
        eventMulticasterEx?.addFocusChangeListener(RichPresenceFocusChangeListener, this)

        // Connect to Discord client
        scope.launch(Dispatchers.IO) {
            reconnect().await()
        }
    }

    /**
     * A [Mutex] for accessing the connection.
     */
    private val mutex: Mutex = Mutex()

    /**
     * A connection with the Discord client.
     */
    private var connection: Core? = null

    /**
     * The latest [ActivityContext] that was changed.
     */
    var activityContext: ActivityContext? = null
        private set

    /**
     * A [MergingRunner] for the [reconnect] function.
     */
    private val reconnectRunner = MergingRunner<Unit>(scope, Dispatchers.IO)

    /**
     * Starts the reconnection process or merges into an existing one.
     * The reconnection process consists of closing the connection and starting a new one.
     * It's executed using [MergingRunner].
     */
    suspend fun reconnect(): Deferred<Unit> = reconnectRunner.run {
        mutex.withLock {
            connection?.close()
        }
        val newConnection = connect()
        mutex.withLock {
            connection = newConnection
        }
        update()
    }

    /**
     * Renders the [ActivityContext] and changes the activity.
     * The [activityContext] parameter can be null for hiding the Rich Presence.
     *
     * The latest activity context is stored and can be updated using [update] function.
     *
     * When the `reconnectOnUpdate` setting is enabled and the connection is closed,
     * the reconnect process is launched.
     */
    suspend fun changeActivity(activityContext: ActivityContext?) {
        val activity = activityContext?.createActivity()
        val success = mutex.withLock {
            this.activityContext = activityContext
            try {
                connection?.activityManager()?.updateActivity(activity) != null
            } catch (_: ClosedChannelException) {
                false
            }
        }

        if (!success && discordSettingsComponent.state.reconnectOnUpdate) {
            @Suppress("DeferredResultUnused")
            reconnect()
        }
    }

    /**
     * Re-creates the stored activity context and updates the activity.
     */
    suspend fun update() {
        mutex.withLock {
            try {
                connection?.activityManager()?.updateActivity(activityContext?.createActivity())
            } catch (_: ClosedChannelException) {}
        }
    }

    override fun dispose() {
        connection?.close()
    }
}