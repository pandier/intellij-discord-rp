package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import io.github.pandier.intellijdiscordrp.service.DiscordService

class RichPresenceFileListener : FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val discordService = DiscordService.getInstance()
        val newFile = event.newFile
        val editor = (event.newEditor as? TextEditor?)?.editor
        discordService.changeActivityBackground(event.manager.project, newFile, editor)
    }
}