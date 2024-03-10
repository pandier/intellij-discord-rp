package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.dsl.builder.COLUMNS_LARGE
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import javax.swing.JPanel

class DiscordSettingsConfigurable : Configurable {
    private var component: JPanel? = null

    override fun getDisplayName(): String = "Discord Rich Presence"

    override fun createComponent(): JComponent = panel {
        group("Project") {
            row("Details:") {
                textField()
                    .columns(COLUMNS_LARGE)
            }
            row("State:") {
                textField()
                    .columns(COLUMNS_LARGE)
            }
        }

        group("File") {
            row("Details:") {
                textField()
                    .columns(COLUMNS_LARGE)
            }
            row("State:") {
                textField()
                    .columns(COLUMNS_LARGE)
            }
        }
    }.also { component = it }

    override fun isModified(): Boolean = false

    override fun apply() {
    }

    override fun reset() {
    }

}