package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.DumbAwareAction
import io.github.pandier.intellijdiscordrp.service.discordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReconnectAction : DumbAwareAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun actionPerformed(e: AnActionEvent) {
        discordService.scope.launch(Dispatchers.EDT) {
            discordService.reconnect()
        }
    }
}