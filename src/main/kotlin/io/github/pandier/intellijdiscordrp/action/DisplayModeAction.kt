package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareToggleAction
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class DisplayModeAction(
    private val displayMode: ActivityDisplayMode,
) : DumbAwareToggleAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun isSelected(e: AnActionEvent): Boolean =
        e.project?.discordProjectSettingsComponent?.state?.displayMode == displayMode

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.project?.discordProjectSettingsComponent?.state?.displayMode = if (state) {
            displayMode
        } else {
            null
        }

        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.IO) {
            discordService.updateActivity()
        }
    }
}

class ApplicationDisplayModeAction : DisplayModeAction(ActivityDisplayMode.APPLICATION)
class ProjectDisplayModeAction : DisplayModeAction(ActivityDisplayMode.PROJECT)
class FileDisplayModeAction : DisplayModeAction(ActivityDisplayMode.FILE)
