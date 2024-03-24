package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupActivity
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.discordService
import io.github.pandier.intellijdiscordrp.service.timeTrackingService

class RichPresenceProjectListener : ProjectManagerListener, StartupActivity {

    override fun projectClosed(project: Project) {
        timeTrackingService.stop(project)
        if (discordService.activityContext?.project?.get() == project) {
            discordService.clearActivity()
        }
    }

    override fun runActivity(project: Project) {
        DiscordRichPresencePlugin.registerEditorListeners()

        if (discordService.activityContext?.project?.get() != project) {
            discordService.changeActivity(ActivityContext.create(project = project))
        }
    }
}