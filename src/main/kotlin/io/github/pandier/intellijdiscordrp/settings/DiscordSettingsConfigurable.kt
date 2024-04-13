package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.TabbedBuilder
import io.github.pandier.intellijdiscordrp.settings.ui.errorOnInput
import io.github.pandier.intellijdiscordrp.settings.ui.tabbed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KMutableProperty0

private fun TabbedBuilder.displayModeTab(
    displayMode: ActivityDisplayMode,
    imageSettings: List<ImageSetting>,
    details: KMutableProperty0<String>,
    state: KMutableProperty0<String>,
    largeImage: KMutableProperty0<ImageSetting>,
    largeImageEnabled: KMutableProperty0<Boolean>,
    largeImageText: KMutableProperty0<String>,
    smallImage: KMutableProperty0<ImageSetting>,
    smallImageEnabled: KMutableProperty0<Boolean>,
    smallImageText: KMutableProperty0<String>,
) {
    tab(displayMode.toString()) {
        row("Details:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(details)
                .also { it.component.emptyText.text = "Optional" }
        }
        row("State:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(state)
                .also { it.component.emptyText.text = "Optional" }
        }

        // Large image settings
        lateinit var largeImageCheckBox: Cell<JBCheckBox>
        row {
            largeImageCheckBox = checkBox("Large image")
                .bindSelected(largeImageEnabled)
        }
        indent {
            row {
                label("Icon:")
                comboBox(imageSettings)
                    .bindItem(largeImage.toNullableProperty())
                label("Text:")
                textField()
                    .bindText(largeImageText)
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isBlank() }
            }
        }.enabledIf(largeImageCheckBox.selected)

        // Small image settings
        lateinit var smallImageCheckBox: Cell<JBCheckBox>
        row {
            smallImageCheckBox = checkBox("Small image")
                .bindSelected(smallImageEnabled)
        }
        indent {
            row {
                label("Icon:")
                comboBox(imageSettings)
                    .bindItem(smallImage.toNullableProperty())
                label("Text:")
                textField()
                    .bindText(smallImageText)
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isBlank() }
            }
        }.enabledIf(smallImageCheckBox.selected)

        row {
            val lines = displayMode.variables.map { variable -> "<code>$variable</code> - ${variable.description}" }
            comment(lines.joinToString("<br/>"))
        }
    }
}

class DiscordSettingsConfigurable : DslConfigurable("Discord Rich Presence") {

    override fun createPanel(): DialogPanel = panel {
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
                .errorOnInput("Must be a valid id") { it.text.isNotEmpty() && it.text.toULongOrNull() == null }
                .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                .errorOnApply("Must be a valid id") { it.isEnabled && it.text.toULongOrNull() == null }
        }

        row {
            label("Default display mode:")
            comboBox(ActivityDisplayMode.values().toList())
                .bindItem(state::defaultDisplayMode.toNullableProperty())
                .comment(
                    "Applied to all projects that don't configure a specific display mode.<br/>" +
                            "Use 'Change Display Mode in Project' action for changing the display mode in a project."
                )
        }

        group("Display") {
            tabbed {
                displayModeTab(
                    displayMode = ActivityDisplayMode.APPLICATION,
                    imageSettings = listOf(ImageSetting.APPLICATION),
                    details = state::applicationDetails,
                    state = state::applicationState,
                    largeImage = state::applicationLargeImage,
                    largeImageEnabled = state::applicationLargeImageEnabled,
                    largeImageText = state::applicationLargeImageText,
                    smallImage = state::applicationSmallImage,
                    smallImageEnabled = state::applicationSmallImageEnabled,
                    smallImageText = state::applicationSmallImageText,
                )

                displayModeTab(
                    displayMode = ActivityDisplayMode.PROJECT,
                    imageSettings = listOf(ImageSetting.APPLICATION),
                    details = state::projectDetails,
                    state = state::projectState,
                    largeImage = state::projectLargeImage,
                    largeImageEnabled = state::projectLargeImageEnabled,
                    largeImageText = state::projectLargeImageText,
                    smallImage = state::projectSmallImage,
                    smallImageEnabled = state::projectSmallImageEnabled,
                    smallImageText = state::projectSmallImageText,
                )

                displayModeTab(
                    displayMode = ActivityDisplayMode.FILE,
                    imageSettings = listOf(ImageSetting.APPLICATION, ImageSetting.FILE),
                    details = state::fileDetails,
                    state = state::fileState,
                    largeImage = state::fileLargeImage,
                    largeImageEnabled = state::fileLargeImageEnabled,
                    largeImageText = state::fileLargeImageText,
                    smallImage = state::fileSmallImage,
                    smallImageEnabled = state::fileSmallImageEnabled,
                    smallImageText = state::fileSmallImageText,
                )
            }
        }
    }

    override fun apply() {
        if (validateAndApply()) {
            val discordService = DiscordService.getInstance()
            discordService.scope.launch(Dispatchers.IO) {
                discordService.reconnect()
            }
        }
    }
}