package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.listener.RichPresenceDocumentListener
import io.github.pandier.intellijdiscordrp.listener.RichPresenceFocusChangeListener
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.util.KPresenceLoggerAdapter
import io.github.pandier.intellijdiscordrp.util.MergingRunner
import io.github.vyfor.kpresence.RichClient
import io.github.vyfor.kpresence.exception.NotConnectedException
import io.github.vyfor.kpresence.exception.PipeNotFoundException
import io.github.vyfor.kpresence.rpc.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private fun connect(): RichClient {
    val settings = discordSettingsComponent.state
    val applicationId = if (settings.customApplicationIdEnabled) {
        settings.customApplicationId.toULong().toLong()
    } else {
        currentActivityApplicationType.discordApplicationId
    }

    return RichClient(applicationId).apply {
        logger = KPresenceLoggerAdapter
        connect()
    }
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

    /**
     * A [Mutex] for accessing the connection.
     */
    private val mutex: Mutex = Mutex()

    /**
     * A connection with the Discord client.
     */
    private var connection: RichClient? = null

    /**
     * The latest [ActivityContext] that was changed.
     */
    private var activityContext: ActivityContext? = null

    /**
     * A [MergingRunner] for the [reconnect] function.
     */
    private val reconnectRunner = MergingRunner<Boolean>()

    init {
        // Register focus change listener
        val eventMulticaster = EditorFactory.getInstance().eventMulticaster
        val eventMulticasterEx = eventMulticaster as? EditorEventMulticasterEx
        eventMulticasterEx?.addFocusChangeListener(RichPresenceFocusChangeListener, this)
        eventMulticasterEx?.addDocumentListener(RichPresenceDocumentListener, this)

        // Connect to Discord client
        reconnectBackground()
    }

    /**
     * Executes the reconnection process in the current coroutine or merges into an existing process.
     * The reconnection process consists of closing the connection and starting a new one.
     * It's executed using [MergingRunner].
     *
     * When [silent] is true, only logs related to state change will be logged.
     */
    suspend fun reconnect(silent: Boolean = false): Deferred<Boolean> = reconnectRunner.run {
        try {
            mutex.withLock {
                connection?.shutdown()
                connection = null
            }

            val newConnection = connect()
            mutex.withLock {
                connection = newConnection
                sendActivityInternal(activityContext?.createActivity())
            }

            DiscordRichPresencePlugin.logger.info("Connected to Discord client")
            true
        } catch (ex: PipeNotFoundException) {
            if (!silent)
                DiscordRichPresencePlugin.logger.info("Could not find any Discord client instance")
            false
        } catch (ex: Exception) {
            if (!silent)
                DiscordRichPresencePlugin.logger.error("Failed to connect", ex)
            throw ex
        }
    }

    /**
     * Executes the reconnection process on the background or merges into an existing one.
     *
     * @see reconnect
     */
    fun reconnectBackground(silent: Boolean = false) {
        scope.launch(Dispatchers.IO) {
            @Suppress("DeferredResultUnused")
            reconnect(silent)
        }
    }

    /**
     * Changes the activity only when the current activity project is different from the given project.
     * This ensures that when this function is run after a file has already been asigned,
     * the file will be kept unless the project is different.
     *
     * Used, for example, in project startup activity.
     *
     * @see modifyActivity
     */
    suspend fun switchProject(project: Project) {
        val activityContext = ActivityContext.create(project, null)
        modifyActivity {
            if (it?.project?.get() != project)
                activityContext
            else it
        }
    }

    /**
     * Runs the [hideProject] on the background in a coroutine with [Dispatchers.Default] context.
     *
     * @see hideProject
     */
    fun hideProjectBackground(project: Project) {
        scope.launch(Dispatchers.Default) {
            hideProject(project)
        }
    }

    /**
     * Hides the activity if the current activity project is the given project.
     * This ensures that the activity won't get destroyed if the acitivity isn't related to the project.
     *
     * @see modifyActivity
     */
    suspend fun hideProject(project: Project) {
        modifyActivity {
            if (it?.project?.get() == project)
                null
            else it
        }
    }

    /**
     * Runs the [changeActivity] on the background in a coroutine with [Dispatchers.Default] context.
     *
     * @see changeActivity
     */
    fun changeActivityBackground(project: Project, file: VirtualFile?, document: Document?) {
        scope.launch(Dispatchers.Default) {
            changeActivity(project, file, document)
        }
    }

    /**
     * Changes the activity to the given project and file.
     * The [Document] object is used for stuff like line count and file size.
     *
     * @see changeActivity
     */
    suspend fun changeActivity(project: Project, file: VirtualFile?, document: Document?) {
        val activityContext = ActivityContext.create(project, file, document)
        changeActivity(activityContext)
    }

    /**
     * Renders the [ActivityContext] and updates the activity.
     * The [ActivityContext] can be null for hiding the Rich Presence.
     *
     * The latest activity context is stored and can be updated using [update] function.
     *
     * When the `reconnectOnUpdate` setting is enabled and the connection is closed,
     * a reconnect process is launched.
     */
    suspend fun changeActivity(activityContext: ActivityContext?) {
        modifyActivity { activityContext }
    }

    /**
     * Modifies the existing activity context and updates the activity.
     * The [ActivityContext] can be null for hiding the Rich Presence.
     *
     * The [block] that modifies the activity context is run under
     * the connection lock and shouldn't do any expensive calculations.
     *
     * The latest activity context is stored and can be updated using [update] function.
     *
     * When the `reconnectOnUpdate` setting is enabled and the connection is closed,
     * a reconnect process is launched.
     */
    suspend fun modifyActivity(block: suspend (ActivityContext?) -> ActivityContext?) {
        val success = mutex.withLock {
            activityContext = block(activityContext)
            sendActivityInternal(activityContext?.createActivity())
        }

        if (!success && discordSettingsComponent.state.reconnectOnUpdate) {
            reconnectBackground(true)
        }
    }

    /**
     * Runs the [update] on the background in a coroutine with [Dispatchers.Default] context.
     *
     * @see update
     */
    fun updateBackground() {
        scope.launch(Dispatchers.Default) {
            update()
        }
    }

    /**
     * Updates the activity.
     */
    suspend fun update() {
        mutex.withLock {
            sendActivityInternal(activityContext?.createActivity())
        }
    }

    private fun sendActivityInternal(activity: Activity?): Boolean {
        return try {
            connection?.update(activity) != null
        } catch (ex: NotConnectedException) {
            connection = null
            false
        } catch (ex: Exception) {
            DiscordRichPresencePlugin.logger.info("An error ocurred while sending activity", ex)
            connection = null
            false
        }
    }

    override fun dispose() {
        connection?.shutdown()
    }
}