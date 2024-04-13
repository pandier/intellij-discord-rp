package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.listener.RichPresenceFocusChangeListener
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
    }

    /**
     * The [ActivityContext] that is currently displayed.
     */
    @Volatile
    var activityContext: ActivityContext? = null
        private set

    /**
     * A conflated channel for communicating [Activity] changes with the connection coroutine.
     *
     * @see launchConnection
     */
    private val activityChannel: Channel<Activity?> = Channel(Channel.CONFLATED)

    /**
     * The connection coroutine that is currently running.
     */
    private var connectionJob = launchConnection()

    /**
     * Returns true if connection coroutine is active.
     */
    val isConnected: Boolean
        get() = connectionJob.isActive

    /**
     * A [Mutex] that is locked when a connection is being established.
     */
    private val reconnectMutex = Mutex(true)

    /**
     * Launches a new coroutine on [Dispatchers.IO] that connects
     * to the Discord client and receives activites from [activityChannel]
     * and sends them to the client.
     *
     * The connection is closed if an error happens while sending
     * the activity or the coroutine is cancelled.
     *
     * The [reconnectMutex] is unlocked when a connection
     * with the Discord client was created or failed creating.
     */
    private fun launchConnection() = scope.launch(Dispatchers.IO) {
        var reconnectUnlocked = false
        try {
            connect()?.use {
                if (reconnectMutex.isLocked) {
                    reconnectUnlocked = true
                    reconnectMutex.unlock()
                }

                for (activity in activityChannel) {
                    try {
                        it.activityManager()?.updateActivity(activity)
                    } catch (ex: RuntimeException) {
                        // INFO level because error also happens when the Discord client is closed
                        DiscordRichPresencePlugin.logger.info("An error happened while updating activity", ex)
                        break
                    }
                }
            }
        } finally {
            if (!reconnectUnlocked && reconnectMutex.isLocked)
                reconnectMutex.unlock()
        }
    }

    /**
     * Closes the current connection and launches a new one.
     * Suspends until a connection was achieved with the client.
     *
     * When this function is called while a reconnecting process is already happening,
     * it doesn't start a new reconnecting process, but suspends until the existing one has finished.
     *
     * When the [update] parameter is true, [updateActivity] will be called after reconnecting
     * with the `enableReconnecting` parameter set as false.
     *
     * This function is thread-safe.
     */
    suspend fun reconnect(update: Boolean = true) {
        // If already reconnecting
        if (reconnectMutex.isLocked) {
            // Wait until reconnect is finished
            reconnectMutex.withLock {}
            return
        }

        // Start reconnecting
        reconnectMutex.lock()
        if (isConnected)
            connectionJob.cancelAndJoin()
        connectionJob = launchConnection()

        // Wait until reconnect mutex is unlocked by connection coroutine
        reconnectMutex.withLock {}

        if (update)
            updateActivity()
    }

    /**
     * Renders the [ActivityContext] and changes the activity.
     * The [activityContext] parameter can be null for hiding the Rich Presence.
     *
     * The activity context is stored and can be re-rendered using [updateActivity] function.
     *
     * When the `reconnectOnUpdate` setting is enabled, [reconnect] is called when not connected
     * and suspends this function until the reconnect process has finished.
     */
    suspend fun changeActivity(activityContext: ActivityContext?) {
        val projectSettings = activityContext?.project?.get()?.discordProjectSettingsComponent?.state
        val activity = when {
            projectSettings != null && projectSettings.showRichPresence -> {
                val defaultDisplayMode = discordSettingsComponent.state.defaultDisplayMode
                val displayMode = ActivityDisplayMode.getSupportedFrom(
                    projectSettings.displayMode ?: defaultDisplayMode,
                    activityContext
                )
                discordSettingsComponent.state.getActivityFactory(displayMode)
                    .create(activityContext)
            }

            else -> null
        }

        this.activityContext = activityContext
        activityChannel.send(activity)

        if (!isConnected && discordSettingsComponent.state.reconnectOnUpdate)
            reconnect(false)
    }

    /**
     * Clears the activity. Equals to:
     *
     * ```
     * discordService.changeActivity(null)
     * ```
     *
     * @see changeActivity
     */
    suspend fun clearActivity() =
        changeActivity(null)

    /**
     * Re-renders the latest [ActivityContext] and updates the activity. Equals to:
     *
     * ```
     * discordService.changeActivity(discordService.activityContext)
     * ```
     *
     * @see changeActivity
     */
    suspend fun updateActivity() =
        changeActivity(activityContext)

    override fun dispose() {
        connectionJob.cancel()
        activityChannel.close()
    }
}