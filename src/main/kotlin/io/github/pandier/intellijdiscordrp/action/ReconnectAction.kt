package io.github.pandier.intellijdiscordrp.action

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.kpresence.KPresenceClient
import io.github.pandier.kpresence.exception.DiscordNotFoundException
import io.github.pandier.intellijdiscordrp.DiscordRichPresenceBundle
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

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, DiscordRichPresenceBundle.message("progress.title.reconnecting"), false) {
            override fun run(indicator: ProgressIndicator) {
                try {
                    val discordService = DiscordService.getInstance()
                    val result = runBlocking {
                        discordService.client.reconnect().await()
                    }

                    when (result) {
                        KPresenceClient.ConnectResult.Success -> {
                            NotificationGroupManager.getInstance()
                                .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                                .createNotification(
                                    DiscordRichPresenceBundle.message("notification.title.reconnect"),
                                    DiscordRichPresenceBundle.message("notification.content.reconnect.success"),
                                    NotificationType.INFORMATION
                                )
                                .notify(project)
                        }
                        is KPresenceClient.ConnectResult.Failed -> {
                            if (result.exception is DiscordNotFoundException) {
                                NotificationGroupManager.getInstance()
                                    .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                                    .createNotification(
                                        DiscordRichPresenceBundle.message("notification.title.reconnect"),
                                        DiscordRichPresenceBundle.message("notification.content.reconnect.missingClient"),
                                        NotificationType.WARNING
                                    )
                                    .notify(project)
                            } else {
                                NotificationGroupManager.getInstance()
                                    .getNotificationGroup("io.github.pandier.intellijdiscordrp.notification.Reconnecting")
                                    .createNotification(
                                        DiscordRichPresenceBundle.message("notification.title.reconnect"),
                                        result.exception.message?.let { DiscordRichPresenceBundle.message("notification.content.reconnect.failedError", it) }
                                            ?: DiscordRichPresenceBundle.message("notification.content.reconnect.failed"),
                                        NotificationType.ERROR
                                    )
                                    .notify(project)
                            }
                        }
                        else -> {}
                    }
                } finally {
                    mutex.unlock()
                }
            }
        })
    }
}