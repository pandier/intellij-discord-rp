package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

val discordSettingsState: DiscordSettingsState
    get() = service()

@State(
    name = "io.github.pandier.intellijdiscordrp.settings.DiscordSettingsState",
    storages = [Storage("discordrp.xml")]
)
class DiscordSettingsState : PersistentStateComponent<DiscordSettings> {
    private val state = DiscordSettings()

    override fun getState(): DiscordSettings = state

    override fun loadState(state: DiscordSettings) =
        XmlSerializerUtil.copyBean(state, this.state)
}
