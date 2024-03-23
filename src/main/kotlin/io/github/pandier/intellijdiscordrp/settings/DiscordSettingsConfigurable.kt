package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import io.github.pandier.intellijdiscordrp.service.discordService
import javax.swing.JComponent

class DiscordSettingsConfigurable : Configurable {
    private val panel = panel {
        val state = discordSettingsComponent.state

        row {
            checkBox("Try reconnecting on activity update")
                .bindSelected(state::reconnectOnUpdate)
        }

        // Project activity factory settings
        group("Project") {
            row("Details:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(state::projectDetails)
            }
            row("State:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(state::projectState)
            }

            // Large image settings
            lateinit var largeImageCheckBox: Cell<JBCheckBox>
            row {
                largeImageCheckBox = checkBox("Large image")
                    .bindSelected(state::projectLargeImageEnabled)
            }.enabledIf(largeImageCheckBox.selected)
            indent {
                row {
                    label("Image:")
                    comboBox(listOf(IconTypeSetting.APPLICATION))
                        .bindItem(state::projectLargeImage.toNullableProperty())
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
                    comboBox(listOf(IconTypeSetting.APPLICATION))
                        .bindItem(state::projectSmallImage.toNullableProperty())
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
                    .align(AlignX.FILL)
                    .bindText(state::fileDetails)
            }
            row("State:") {
                textField()
                    .align(AlignX.FILL)
                    .bindText(state::fileState)
            }

            // Large image settings
            lateinit var largeImageCheckBox: Cell<JBCheckBox>
            row {
                largeImageCheckBox = checkBox("Large image")
                    .bindSelected(state::fileLargeImageEnabled)
            }
            indent {
                row {
                    label("Image:")
                    comboBox(listOf(IconTypeSetting.APPLICATION, IconTypeSetting.FILE))
                        .bindItem(state::fileLargeImage.toNullableProperty())
                    label("Text:")
                    textField()
                        .bindText(state::fileLargeImageText)
                }
            }.enabledIf(largeImageCheckBox.selected)

            // Small image settings
            lateinit var smallImageCheckBox: Cell<JBCheckBox>
            row {
                smallImageCheckBox = checkBox("Small image")
                    .bindSelected(state::fileSmallImageEnabled)
            }
            indent {
                row {
                    label("Image:")
                    comboBox(listOf(IconTypeSetting.APPLICATION, IconTypeSetting.FILE))
                        .bindItem(state::fileSmallImage.toNullableProperty())
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

    override fun apply() {
        panel.apply()
        discordService.updateActivity()
    }

    override fun reset() = panel.reset()
}