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
        val column = event.newPosition.column + 1
        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.Default) {
            discordService.modifyActivity { context ->
                val project = context?.project?.get() ?: return@modifyActivity context
                if (context.file?.line != line || context.file.column != column) {
                    val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return@modifyActivity context
                    ActivityContext.create(project, editor.virtualFile, editor)
                } else {
                    context
                }
            }
        }
    }
}