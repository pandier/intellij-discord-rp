package io.github.pandier.intellijdiscordrp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.discordService
import io.github.pandier.intellijdiscordrp.settings.project.discordProjectSettingsComponent

abstract class DiscordDisplayModeAction(
    private val displayMode: ActivityDisplayMode,
) : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.discordProjectSettingsComponent?.state?.displayMode = displayMode
        discordService.updateActivity()
    }
}

class DiscordApplicationDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.APPLICATION)
class DiscordProjectDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.PROJECT)
class DiscordFileDisplayModeAction : DiscordDisplayModeAction(ActivityDisplayMode.FILE)
