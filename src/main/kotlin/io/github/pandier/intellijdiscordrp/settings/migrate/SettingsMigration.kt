package io.github.pandier.intellijdiscordrp.settings.migrate

import com.intellij.util.xmlb.SkipDefaultsSerializationFilter
import com.intellij.util.xmlb.XmlSerializer
import io.github.pandier.intellijdiscordrp.DiscordRichPresencePlugin
import io.github.pandier.intellijdiscordrp.settings.DiscordSettings
import org.jdom.Element

object SettingsMigration {
    private const val CURRENT_VERSION = 2
    private val lastMigrator = V1to2SettingsMigrator
    private val serializationFilter = SkipDefaultsSerializationFilter(DiscordSettings())

    fun serialize(settings: DiscordSettings): Element? {
        try {
            val element = XmlSerializer.serialize(settings, serializationFilter)
            if (!element.isEmpty)
                element.setAttribute("version", CURRENT_VERSION.toString())
            return element
        } catch (ex: Exception) {
            DiscordRichPresencePlugin.logger.error("Something went wrong during serialization of settings", ex)
            return null
        }
    }

    fun deserialize(element: Element): DiscordSettings {
        try {
            if (element.isEmpty)
                return DiscordSettings()
            var version = element.getAttribute("version")?.value?.toIntOrNull() ?: 1
            if (version >= CURRENT_VERSION) {
                if (version > CURRENT_VERSION)
                    DiscordRichPresencePlugin.logger.warn("Settings version is higher than current version, trying to deserialize normally")
                return XmlSerializer.deserialize(element, DiscordSettings::class.java)
            }
            return migrate(version, CURRENT_VERSION - 1, lastMigrator, element)
        } catch (ex: Exception) {
            DiscordRichPresencePlugin.logger.warn("Something went wrong during deserialization of settings, resseting to defaults", ex)
            return DiscordSettings()
        }
    }

    private fun <P, T> migrate(version: Int, migratorVersion: Int, migrator: SettingsMigrator<P, T>, element: Element): T {
        if (version != migratorVersion) {
            val previousMigrator = migrator.previousMigrator
                ?: error("Migration from version $version to $CURRENT_VERSION is not supported (missing migrator for version ${migratorVersion - 1})")
            val previous = migrate(version, migratorVersion - 1, previousMigrator, element)
            return migrator.migrate(previous)
        }
        return migrateWithElement(element, migrator)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <P, N> migrateWithElement(element: Element, migrator: SettingsMigrator<P, N>): N {
        if (migrator.previousClass == Element::class.java)
            return migrator.migrate(element as P)
        val deserialized = XmlSerializer.deserialize(element, migrator.previousClass)
        return migrator.migrate(deserialized)
    }
}