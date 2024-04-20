package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import io.github.pandier.intellijdiscordrp.service.DiscordService

class RichPresenceProjectListener : ProjectManagerListener {

    override fun projectClosed(project: Project) {
        val discordService = DiscordService.getInstance()
        discordService.hideProjectBackground(project)
    }
}