package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichPresenceProjectListener : ProjectManagerListener {

    override fun projectClosed(project: Project) {
        val discordService = DiscordService.getInstance()
        if (discordService.activityContext?.project?.get() == project) {
            discordService.scope.launch(Dispatchers.IO) {
                discordService.clearActivity()
            }
        }
    }
}