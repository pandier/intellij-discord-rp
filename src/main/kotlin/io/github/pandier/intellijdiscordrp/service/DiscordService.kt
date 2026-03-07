package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.kpresence.KPresenceClient
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.listener.RichPresenceCaretListener
import io.github.pandier.intellijdiscordrp.listener.RichPresenceDocumentListener
import io.github.pandier.intellijdiscordrp.listener.RichPresenceFocusChangeListener
import io.github.pandier.intellijdiscordrp.settings.discordSettingsComponent
import io.github.pandier.intellijdiscordrp.util.KPresenceLoggerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private fun clientId(): Long {
    val settings = discordSettingsComponent.settings
    return if (settings.customApplicationIdEnabled) {
        settings.customApplicationId.toULong().toLong()
    } else if (settings.showFullApplicationName) {
        currentActivityApplicationType.fullNameDiscordApplicationId ?: currentActivityApplicationType.discordApplicationId
    } else {
        currentActivityApplicationType.discordApplicationId
    }
}

/**
 * A service that handles a connection with the Discord client and manages Rich Presence activities.
 */
@Suppress("DeferredResultUnused")
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
    val client: KPresenceClient = KPresenceClient(clientId()) {
        parentScope = scope
        logger = KPresenceLoggerAdapter

        unixPaths {
            // Add Vesktop Flatpak runtime path when on UNIX systems
            System.getenv("XDG_RUNTIME_DIR")?.let {
                add("$it/.flatpak/dev.vencord.Vesktop/xdg-run")
            }
        }
    }

    /**
     * The latest [ActivityContext] that was changed.
     */
    private var activityContext: ActivityContext? = null

    init {
        // Register focus change listener
        val eventMulticaster = EditorFactory.getInstance().eventMulticaster
        val eventMulticasterEx = eventMulticaster as? EditorEventMulticasterEx
        eventMulticasterEx?.addFocusChangeListener(RichPresenceFocusChangeListener, this)
        eventMulticasterEx?.addDocumentListener(RichPresenceDocumentListener, this)
        eventMulticasterEx?.addCaretListener(RichPresenceCaretListener, this)

        // Connect to Discord client
        client.connect()

        // Initialize the idle timeout service
        FocusTimeoutService.getInstance()
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
    fun changeActivityBackground(project: Project, file: VirtualFile?, editor: Editor?) {
        scope.launch(Dispatchers.Default) {
            changeActivity(project, file, editor)
        }
    }

    /**
     * Changes the activity to the given project and file.
     * The [Editor] object is used for stuff like line count and caret position.
     *
     * @see changeActivity
     */
    suspend fun changeActivity(project: Project, file: VirtualFile?, editor: Editor?) {
        val activityContext = ActivityContext.create(project, file, editor)
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
        mutex.withLock {
            val newActivityContext = block(activityContext)
            if (newActivityContext == activityContext)
                return@withLock true
            activityContext = newActivityContext
            client.update(activityContext?.createActivity())
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
        mutex.withLock<Unit> {
            client.update(activityContext?.createActivity())
        }
    }

    /**
     * Temporarily hides the activity without modifying the current activity context.
     * This is used for example when hiding the activity after IDE focus loss.
     *
     * The activity can be shown again by calling [update].
     */
    fun hide() {
        client.update(null)
    }

    override fun dispose() {
        client.close()
    }
}