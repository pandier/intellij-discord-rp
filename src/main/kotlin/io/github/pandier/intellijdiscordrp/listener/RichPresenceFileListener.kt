package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.service.discordService

class RichPresenceFileListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        discordService.changeActivity(source.project) {
            this.file = file
        }
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        discordService.changeActivity(source.project)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) {
            fileClosed(event.manager, event.oldFile)
            return
        }
        discordService.changeActivity(event.manager.project) {
            this.file = event.newFile
        }
    }
}