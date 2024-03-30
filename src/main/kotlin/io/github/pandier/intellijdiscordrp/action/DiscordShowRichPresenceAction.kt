package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareToggleAction
import io.github.pandier.intellijdiscordrp.service.discordService
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent

class DiscordShowRichPresenceAction : DumbAwareToggleAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun isSelected(e: AnActionEvent): Boolean =
        e.project?.discordProjectSettingsComponent?.state?.showRichPresence ?: true

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.project?.discordProjectSettingsComponent?.state?.showRichPresence = state
        discordService.updateActivity()
    }
}