package io.github.pandier.intellijdiscordrp.action

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReconnectAction : DumbAwareAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val discordService = DiscordService.getInstance()

        discordService.scope.launch(Dispatchers.EDT) {
            @Suppress("UnstableApiUsage")
            withBackgroundProgress(project, "Reconnecting Discord client", false) {
                discordService.reconnect()

                NotificationGroupManager.getInstance()
                    .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                    .createNotification("Reconnected with Discord client", "", NotificationType.INFORMATION)
                    .notify(project)
            }
        }
    }
}