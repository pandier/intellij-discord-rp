package io.github.pandier.intellijdiscordrp.settings.project

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

val Project.discordProjectSettingsComponent: DiscordProjectSettingsComponent
    get() = service()

@Service(Service.Level.PROJECT)
@State(
    name = "io.github.pandier.intellijdiscordrp.settings.project.DiscordProjectSettingsComponent",
    storages = [Storage("discordrp.xml")]
)
class DiscordProjectSettingsComponent : PersistentStateComponent<DiscordProjectSettings> {
    private val state = DiscordProjectSettings()

    override fun getState(): DiscordProjectSettings =
        state

    override fun loadState(state: DiscordProjectSettings) =
        XmlSerializerUtil.copyBean(state, this.state)
}