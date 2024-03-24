package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.fileEditor.FileEditorManager
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.discordService

object RichPresenceFocusChangeListener : FocusChangeListener {

    override fun focusGained(editor: Editor) {
        val project = editor.project
        if (project != null && discordService.activityContext?.project != project) {
            val fileEditorManager = FileEditorManager.getInstance(project)
            val file = fileEditorManager.selectedFiles.firstOrNull()
            discordService.changeActivity(ActivityContext.create(project = project, file = file))
        }
    }
}