package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.dsl.builder.COLUMNS_LARGE
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class DiscordSettingsConfigurable : Configurable {
    private val panel = panel {
        val state = discordSettingsState.state

        group("Project") {
            row("Details:") {
                textField()
                    .columns(COLUMNS_LARGE)
                    .bindText(state::projectDetails)
            }
            row("State:") {
                textField()
                    .columns(COLUMNS_LARGE)
                    .bindText(state::projectState)
            }
        }

        group("File") {
            row("Details:") {
                textField()
                    .columns(COLUMNS_LARGE)
                    .bindText(state::fileDetails)
            }
            row("State:") {
                textField()
                    .columns(COLUMNS_LARGE)
                    .bindText(state::fileState)
            }
        }
    }

    override fun getDisplayName(): String = "Discord Rich Presence"

    override fun createComponent(): JComponent = panel

    override fun isModified(): Boolean = panel.isModified()

    override fun apply() = panel.apply()

    override fun reset() = panel.reset()
}