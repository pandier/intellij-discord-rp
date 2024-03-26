package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.discordService

class RichPresenceFileListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        discordService.changeActivity(ActivityContext.create(project = source.project, file = file))
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        discordService.changeActivity(ActivityContext.create(project = source.project))
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) {
            fileClosed(event.manager, event.oldFile)
        } else {
            fileOpened(event.manager, event.newFile)
        }
    }
}