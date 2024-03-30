package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareToggleAction
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent

abstract class DiscordDisplayModeAction(
    private val displayMode: ActivityDisplayMode,
) : DumbAwareToggleAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.BGT


    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun isSelected(e: AnActionEvent): Boolean =
        e.project?.discordProjectSettingsComponent?.state?.displayMode == displayMode

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        if (state) {
            e.project?.discordProjectSettingsComponent?.state?.displayMode = displayMode
        }
    }
}

class DiscordApplicationDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.APPLICATION)
class DiscordProjectDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.PROJECT)
class DiscordFileDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.FILE)
