package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import io.github.pandier.intellijdiscordrp.settings.migrate.SettingsMigration
import org.jdom.Element

val discordSettingsComponent: DiscordSettingsComponent
    get() = service()

@Service
@State(
    name = "io.github.pandier.intellijdiscordrp.settings.DiscordSettingsComponent",
    storages = [Storage("discordrp.xml")]
)
class DiscordSettingsComponent : PersistentStateComponent<Element> {
    val settings = DiscordSettings()

    fun setSettings(state: DiscordSettings) {
        XmlSerializerUtil.copyBean(state, this.settings)
    }

    override fun getState(): Element? =
        SettingsMigration.serialize(this.settings)

    override fun loadState(state: Element) {
        setSettings(SettingsMigration.deserialize(state))
    }
}
