package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RichPresenceCaretListener : CaretListener {
    override fun caretPositionChanged(event: CaretEvent) {
        val line = event.newPosition.line + 1
        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.Default) {
            discordService.modifyActivity { context ->
                val project = context?.project?.get()
                val editor = project?.let { FileEditorManager.getInstance(it) }?.selectedTextEditor
                if (context?.file?.line != line && editor != null) {
                    ActivityContext.create(project, editor.virtualFile, editor)
                } else {
                    context
                }
            }
        }
    }
}