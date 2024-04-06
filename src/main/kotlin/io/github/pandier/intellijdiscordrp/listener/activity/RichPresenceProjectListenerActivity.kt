package io.github.pandier.intellijdiscordrp.listener.activity

import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichPresenceProjectListenerActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.EDT) {
            if (discordService.activityContext?.project?.get() != project) {
                val context = ActivityContext.create(project = project)
                discordService.changeActivity(context)
            }
        }
    }
}