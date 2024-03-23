package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

val discordSettingsComponent: DiscordSettingsComponent
    get() = service()

@Service
@State(
    name = "io.github.pandier.intellijdiscordrp.settings.DiscordSettingsComponent",
    storages = [Storage("discordrp.xml")]
)
class DiscordSettingsComponent : PersistentStateComponent<DiscordSettings> {
    private val state = DiscordSettings()

    override fun getState(): DiscordSettings = state

    override fun loadState(state: DiscordSettings) =
        XmlSerializerUtil.copyBean(state, this.state)
}
