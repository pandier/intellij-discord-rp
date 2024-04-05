package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.service.discordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichPresenceFileListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val context = ActivityContext.create(project = source.project, file = file)
        discordService.scope.launch(Dispatchers.EDT) {
            discordService.changeActivity(context)
        }
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        val context = ActivityContext.create(project = source.project)
        discordService.scope.launch(Dispatchers.EDT) {
            discordService.changeActivity(context)
        }
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