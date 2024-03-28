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

        row {
            val customApplicationIdCheckBox = checkBox("Custom application id:")
                .bindSelected(state::customApplicationIdEnabled)
            textField()
                .bindText(state::customApplicationId)
                .enabledIf(customApplicationIdCheckBox.selected)
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
            }
            indent {
                row {
                    label("Image:")
                    comboBox(listOf(ImageSetting.APPLICATION))
                        .bindItem(state::projectLargeImage.toNullableProperty())
                    label("Text:")
                    textField()
                        .bindText(state::projectLargeImageText)
                }
            }.enabledIf(largeImageCheckBox.selected)

            // Small image settings
            lateinit var smallImageCheckBox: Cell<JBCheckBox>
            row {
                smallImageCheckBox = checkBox("Small image")
                    .bindSelected(state::projectSmallImageEnabled)
            }
            indent {
                row {
                    label("Image:")
                    comboBox(listOf(ImageSetting.APPLICATION))
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
                    comboBox(listOf(ImageSetting.APPLICATION, ImageSetting.FILE))
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
                    comboBox(listOf(ImageSetting.APPLICATION, ImageSetting.FILE))
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
        discordService.reconnect()
    }

    override fun reset() = panel.reset()
}