package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.intellijdiscordrp.service.discordService

class DiscordReconnectAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        discordService.reconnect()
    }
}