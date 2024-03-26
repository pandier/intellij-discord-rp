package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.github.pandier.intellijdiscordrp.service.discordService

class DiscordReconnectAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        discordService.reconnect()
    }
}