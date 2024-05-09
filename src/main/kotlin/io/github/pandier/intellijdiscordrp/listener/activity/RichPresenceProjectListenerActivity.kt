package io.github.pandier.intellijdiscordrp.listener.activity

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.github.pandier.intellijdiscordrp.service.DiscordService

class RichPresenceProjectListenerActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val discordService = DiscordService.getInstance()
        discordService.switchProject(project)
    }
}