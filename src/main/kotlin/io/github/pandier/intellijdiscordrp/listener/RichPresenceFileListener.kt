package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import io.github.pandier.intellijdiscordrp.service.DiscordService

class RichPresenceFileListener : FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val newFile = event.newFile
        val discordService = DiscordService.getInstance()
        discordService.changeActivityBackground(event.manager.project, newFile)
    }
}