package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.util.concurrency.annotations.RequiresEdt
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val discordService: DiscordService
    get() = service()

private fun connect(): Core? = runCatching {
    val settings = discordSettingsComponent.state
    val applicationId = if (settings.customApplicationIdEnabled) {
        settings.customApplicationId.toLong()
    } else {
        currentActivityApplicationType.discordApplicationId
    }

    val internal = Core(CreateParams().apply {
        clientID = applicationId
        flags = CreateParams.getDefaultFlags()
    })

    DiscordRichPresencePlugin.logger.info("Connected to Discord Client")
    return internal
}.getOrElse {
    DiscordRichPresencePlugin.logger.debug("Failed to connect to Discord Client", it)
    null
}

/**
 * A service that handles a connection with the Discord client and manages Rich Presence activities.
 * The current implementation is not thread-safe and requires to be run on the Event Dispatch Thread.
 */
@Service
class DiscordService(
    private val scope: CoroutineScope,
) : Disposable {

    /**
     * The [ActivityContext] that is currently displayed.
     *
     * This property can only be accessed from the EDT thread.
     */
    var activityContext: ActivityContext? = null
        @RequiresEdt get
        @RequiresEdt private set

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
     * Launches a new coroutine on [Dispatchers.IO] that connects
     * to the Discord client and receives activites from [activityChannel]
     * and sends them to the client.
     *
     * The connection is closed if an error happens while sending
     * the activity or the coroutine is cancelled.
     */
    private fun launchConnection() = scope.launch(Dispatchers.IO) {
        connect()?.use {
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
    }

    /**
     * Closes the current connection and launches a new one.
     * Suspends only until the connection is closed, so this function
     * finishes before successfully connected to the client.
     *
     * This function is not thread-safe and requires to be run on the Event Dispatch Thread.
     */
    @RequiresEdt
    suspend fun reconnect() {
        if (isConnected)
            connectionJob.cancelAndJoin()
        connectionJob = launchConnection()
    }

    /**
     * Renders the [ActivityContext] and changes the activity.
     * The [activityContext] parameter can be null for hiding the Rich Presence.
     *
     * The activity context is stored and can be re-rendered using [updateActivity] function.
     *
     * When the `reconnectOnUpdate` setting is enabled, [reconnect] is called
     * and can suspend this function until the connection is closed.
     *
     * This function is not thread-safe and requires to be run on the Event Dispatch Thread.
     */
    @RequiresEdt
    suspend fun changeActivity(activityContext: ActivityContext?) {
        this.activityContext = activityContext

        val projectSettings = activityContext?.project?.get()?.discordProjectSettingsComponent?.state
        val activity = when {
            projectSettings != null && projectSettings.showRichPresence -> {
                val defaultDisplayMode = discordSettingsComponent.state.defaultDisplayMode
                val displayMode = (projectSettings.displayMode ?: defaultDisplayMode).getLower(activityContext.highestSupportedDisplayMode)
                discordSettingsComponent.state.getActivityFactory(displayMode)
                    .create(activityContext)
            }
            else -> null
        }

        // We can run blocking here, the send function of a conflated channel doesn't actually suspend
        runBlocking {
            activityChannel.send(activity)
        }

        if (!isConnected && discordSettingsComponent.state.reconnectOnUpdate)
            reconnect()
    }

    /**
     * Clears the activity. Equals to:
     *
     * ```
     * discordService.changeActivity(null)
     * ```
     *
     * This function is not thread-safe and requires to be run on the Event Dispatch Thread.
     *
     * @see changeActivity
     */
    @RequiresEdt
    suspend fun clearActivity() =
        changeActivity(null)

    /**
     * Re-renders the latest [ActivityContext] and updates the activity. Equals to:
     *
     * ```
     * discordService.changeActivity(discordService.activityContext)
     * ```
     *
     * This function is not thread-safe and requires to be run on the Event Dispatch Thread.
     *
     * @see changeActivity
     */
    @RequiresEdt
    suspend fun updateActivity() =
        changeActivity(activityContext)

    override fun dispose() {
        connectionJob.cancel()
        activityChannel.close()
    }
}