package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.service.DiscordService

class RichPresenceFileListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val discordService = DiscordService.getInstance()
        val project = source.project
        discordService.changeActivityBackground(project, file)
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        val discordService = DiscordService.getInstance()
        val project = source.project
        discordService.changeActivityBackground(project, null)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val newFile = event.newFile
        val oldFile = event.oldFile
        if (newFile == null && oldFile != null) {
            fileClosed(event.manager, oldFile)
        } else if (newFile != null) {
            fileOpened(event.manager, newFile)
        }
    }
}