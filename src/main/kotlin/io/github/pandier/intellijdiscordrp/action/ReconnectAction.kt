package io.github.pandier.intellijdiscordrp.action

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class ReconnectAction : DumbAwareAction() {
    private val mutex = Mutex()

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val discordService = DiscordService.getInstance()

        discordService.scope.launch(Dispatchers.EDT) {
            if (!mutex.tryLock())
                return@launch

            @Suppress("UnstableApiUsage")
            withBackgroundProgress(project, "Reconnecting Discord client", false) {
                try {
                    discordService.reconnect().await()

                    NotificationGroupManager.getInstance()
                        .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                        .createNotification("Reconnected with Discord client", "", NotificationType.INFORMATION)
                        .notify(project)
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
                }
            }

            mutex.unlock()
        }
    }
}