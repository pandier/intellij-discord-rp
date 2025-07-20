package io.github.pandier.intellijdiscordrp.settings.migrate

/**
 * Migrates settings from one version to another.
 */
interface SettingsMigrator<P, N> {
    val version: Int
    val previousClass: Class<P>
    val previousMigrator: SettingsMigrator<*, P>?

    fun migrate(previous: P): N
}