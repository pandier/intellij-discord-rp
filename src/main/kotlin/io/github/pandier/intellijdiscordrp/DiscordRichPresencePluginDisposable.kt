package io.github.pandier.intellijdiscordrp

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service

val discordRichPresencePluginDisposable: DiscordRichPresencePluginDisposable
    get() = service()

@Service
class DiscordRichPresencePluginDisposable : Disposable {
    override fun dispose() {}
}