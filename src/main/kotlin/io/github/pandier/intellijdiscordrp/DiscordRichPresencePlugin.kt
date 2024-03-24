package io.github.pandier.intellijdiscordrp

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import io.github.pandier.intellijdiscordrp.listener.RichPresenceFocusChangeListener

object DiscordRichPresencePlugin {
    val logger = Logger.getInstance(DiscordRichPresencePlugin.javaClass)

    private var editorListenersRegistered = false

    fun registerEditorListeners() {
        if (editorListenersRegistered)
            return
        val eventMulticaster = EditorFactory.getInstance().eventMulticaster
        val eventMulticasterEx = eventMulticaster as? EditorEventMulticasterEx
        eventMulticasterEx?.addFocusChangeListener(RichPresenceFocusChangeListener) {}
        editorListenersRegistered = true
    }
}