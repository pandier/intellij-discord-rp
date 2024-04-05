package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupActivity
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichPresenceProjectListener : ProjectManagerListener, StartupActivity {

    override fun projectClosed(project: Project) {
        val discordService = DiscordService.getInstance()
        if (discordService.activityContext?.project?.get() == project) {
            discordService.scope.launch(Dispatchers.EDT) {
                discordService.clearActivity()
            }
        }
    }

    override fun runActivity(project: Project) {
        DiscordRichPresencePlugin.registerEditorListeners()

        val discordService = DiscordService.getInstance()
        if (discordService.activityContext?.project?.get() != project) {
            val context = ActivityContext.create(project = project)
            discordService.scope.launch(Dispatchers.EDT) {
                discordService.changeActivity(context)
            }
        }
    }
}