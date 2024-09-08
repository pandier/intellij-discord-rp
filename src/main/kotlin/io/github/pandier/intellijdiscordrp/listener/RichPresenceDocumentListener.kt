package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileEditorManager
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RichPresenceDocumentListener : DocumentListener {
    override fun documentChanged(event: DocumentEvent) {
        val lineCount = event.document.lineCount
        val discordService = DiscordService.getInstance()
        discordService.scope.launch(Dispatchers.Default) {
            discordService.modifyActivity { context ->
                val project = context?.project?.get()
                val editor = project?.let { FileEditorManager.getInstance(it) }?.selectedTextEditor
                if (context?.file?.lineCount != lineCount && editor != null) {
                    ActivityContext.create(project, editor.virtualFile, editor)
                } else {
                    context
                }
            }
        }
    }
}