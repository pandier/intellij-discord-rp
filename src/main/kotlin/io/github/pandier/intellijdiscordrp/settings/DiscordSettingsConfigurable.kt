package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class DiscordSettingsConfigurable : Configurable {
    private val panel = panel {
        val state = discordSettingsComponent.state

        // Project activity factory settings
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

            // Large image settings
            row {
                label("Large image")
            }
            indent {
                row {
                    label("Image:")
                    textField()
                        .bindText(state::projectLargeImage)
                    label("Text:")
                    textField()
                        .bindText(state::projectLargeImageText)
                }
            }

            // Small image settings
            lateinit var smallImageCheckBox: Cell<JBCheckBox>
            row {
                smallImageCheckBox = checkBox("Small image")
                    .bindSelected(state::projectSmallImageEnabled)
            }
            indent {
                row {
                    label("Image:")
                    textField()
                        .bindText(state::projectSmallImage)
                    label("Text:")
                    textField()
                        .bindText(state::projectSmallImageText)
                }
            }.enabledIf(smallImageCheckBox.selected)
        }

        // File activity factory settings
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

            // Large image settings
            row {
                label("Large image")
            }
            indent {
                row {
                    label("Image:")
                    textField()
                        .bindText(state::fileLargeImage)
                    label("Text:")
                    textField()
                        .bindText(state::fileLargeImageText)
                }
            }

            // Small image settings
            lateinit var smallImageCheckBox: Cell<JBCheckBox>
            row {
                smallImageCheckBox = checkBox("Small image")
                    .bindSelected(state::fileSmallImageEnabled)
            }
            indent {
                row {
                    label("Image:")
                    textField()
                        .bindText(state::fileSmallImage)
                    label("Text:")
                    textField()
                        .bindText(state::fileSmallImageText)
                }
            }.enabledIf(smallImageCheckBox.selected)
        }
    }

    override fun getDisplayName(): String = "Discord Rich Presence"

    override fun createComponent(): JComponent = panel

    override fun isModified(): Boolean = panel.isModified()

    override fun apply() = panel.apply()

    override fun reset() = panel.reset()
}