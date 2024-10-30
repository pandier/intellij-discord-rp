package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.IdeFrame
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A service that takes care of hiding the activity after the IDE loses focus for a certain amount of time.
 */
@Service
class IdleTimeoutService(
    val scope: CoroutineScope
) : Disposable {
    companion object {
        @JvmStatic
        fun getInstance(): IdleTimeoutService = service()
    }

    private var task: Job? = null

    init {
        val messageBus = ApplicationManager.getApplication().messageBus
        val messageBusConnection = messageBus.connect(scope)
        messageBusConnection.subscribe(ApplicationActivationListener.TOPIC, object : ApplicationActivationListener {
            override fun applicationActivated(ideFrame: IdeFrame) {
                cancelTimeout()
            }

            override fun applicationDeactivated(ideFrame: IdeFrame) {
                scheduleTimeout()
            }
        })
    }

    /**
     * Schedules a task to hide the activity after 10 seconds.
     * This task can be cancelled using [cancelTimeout].
     */
    private fun scheduleTimeout() {
        task?.cancel()
        task = scope.launch {
            delay(10000)
            DiscordRichPresencePlugin.logger.warn("Application lost focus for 10 seconds, hiding activity")
            DiscordService.getInstance().hide()
        }
    }

    /**
     * Cancels the current timeout task (if there is one) and updates the activity.
     */
    private fun cancelTimeout() {
        task?.cancel()
        scope.launch {
            DiscordService.getInstance().update()
        }
    }

    override fun dispose() {
        // No need to cancel the task, scope gets automatically cancelled
    }
}