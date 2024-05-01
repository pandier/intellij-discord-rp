package io.github.pandier.intellijdiscordrp.action

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex

class ReconnectAction : DumbAwareAction() {
    private val mutex = Mutex()

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        if (!mutex.tryLock())
            return

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Reconnecting Discord client", false) {
            override fun run(indicator: ProgressIndicator) {
                val discordService = DiscordService.getInstance()

                try {
                    val result = runBlocking {
                        discordService.reconnect().await()
                    }

                    if (result) {
                        NotificationGroupManager.getInstance()
                            .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                            .createNotification("Reconnected with Discord client", "", NotificationType.INFORMATION)
                            .notify(project)
                    } else {
                        NotificationGroupManager.getInstance()
                            .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                            .createNotification(
                                "Discord reconnect",
                                "Could not find any Discord client instance",
                                NotificationType.WARNING
                            )
                            .notify(project)
                    }
                } catch (ex: Exception) {
                    DiscordRichPresencePlugin.logger.warn("Failed to reconnect with Discord client", ex)
                    NotificationGroupManager.getInstance()
                        .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                        .createNotification(
                            "Failed to reconnect with Discord client",
                            ex.message ?: "",
                            NotificationType.ERROR
                        )
                        .notify(project)
                } finally {
                    mutex.unlock()
                }
            }
        })
    }
}