package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupActivity
import io.github.pandier.intellijdiscordrp.service.discordService
import io.github.pandier.intellijdiscordrp.service.timeTrackingService

class RichPresenceProjectListener : ProjectManagerListener, StartupActivity {

    override fun projectClosed(project: Project) {
        timeTrackingService.stop(project)
        discordService.clearActivity()
    }

    override fun runActivity(project: Project) {
        discordService.changeActivity(project)
    }
}