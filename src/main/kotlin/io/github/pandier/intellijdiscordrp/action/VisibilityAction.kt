package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareToggleAction
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent

abstract class VisibilityAction(
    private val visibility: ActivityDisplayMode?,
) : DumbAwareToggleAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return e.project?.discordProjectSettingsComponent?.state?.displayMode == visibility
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.project?.discordProjectSettingsComponent?.state?.displayMode = if (state) visibility else null

        val discordService = DiscordService.getInstance()
        discordService.updateBackground()
    }
}

class DefaultVisibilityAction : VisibilityAction(null)
class HiddenVisibilityAction : VisibilityAction(ActivityDisplayMode.HIDDEN)
class ApplicationVisibilityAction : VisibilityAction(ActivityDisplayMode.APPLICATION)
class ProjectVisibilityAction : VisibilityAction(ActivityDisplayMode.PROJECT)
class FileVisibilityAction : VisibilityAction(ActivityDisplayMode.FILE)
