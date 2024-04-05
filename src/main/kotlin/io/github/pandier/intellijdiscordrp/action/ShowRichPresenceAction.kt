package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.DumbAwareToggleAction
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowRichPresenceAction : DumbAwareToggleAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun isSelected(e: AnActionEvent): Boolean =
        e.project?.discordProjectSettingsComponent?.state?.showRichPresence ?: true

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.project?.discordProjectSettingsComponent?.state?.showRichPresence = state

        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.EDT) {
            discordService.updateActivity()
        }
    }
}