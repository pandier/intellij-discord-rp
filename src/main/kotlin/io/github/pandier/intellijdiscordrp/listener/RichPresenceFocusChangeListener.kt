package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.fileEditor.FileEditorManager
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RichPresenceFocusChangeListener : FocusChangeListener {

    override fun focusGained(editor: Editor) {
        val project = editor.project
        val discordService = DiscordService.getInstance()
        if (project != null && discordService.activityContext?.project != project) {
            val fileEditorManager = FileEditorManager.getInstance(project)
            val file = fileEditorManager.selectedFiles.firstOrNull()

            val context = ActivityContext.create(project = project, file = file)
            discordService.scope.launch(Dispatchers.EDT) {
                discordService.changeActivity(context)
            }
        }
    }
}