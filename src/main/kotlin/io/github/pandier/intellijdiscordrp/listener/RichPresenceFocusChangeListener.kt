package io.github.pandier.intellijdiscordrp.listener

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import io.github.pandier.intellijdiscordrp.service.DiscordService

object RichPresenceFocusChangeListener : FocusChangeListener {

    override fun focusGained(editor: Editor) {
        val project = editor.project
        val discordService = DiscordService.getInstance()
        if (project != null) {
            val file = editor.virtualFile
            discordService.changeActivityBackground(project, file)
        }
    }
}