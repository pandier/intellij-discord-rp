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
    timestampEnabled: KMutableProperty0<Boolean>,
) {
    tab(displayMode.toString()) {
        row("Details:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(details)
                .errorOnInput("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                .errorOnApply("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                .also { it.component.emptyText.text = "Optional" }
        }
        row("State:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(state)
                .errorOnInput("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                .errorOnApply("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                .also { it.component.emptyText.text = "Optional" }
        }

        // Large image settings
        lateinit var largeImageCheckBox: Cell<JBCheckBox>
        row {
            largeImageCheckBox = checkBox("Show large image")
                .bindSelected(largeImageEnabled)
        }
        indent {
            row {
                comboBox(imageSettings)
                    .label("Icon:")
                    .bindItem(largeImage.toNullableProperty())
                textField()
                    .label("Text:")
                    .bindText(largeImageText)
                    .errorOnInput("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                    .errorOnApply("Length must be between 2 and 128") { it.isEnabled && it.text.length !in 2..128 }
            }
        }.enabledIf(largeImageCheckBox.selected)

        // Small image settings
        lateinit var smallImageCheckBox: Cell<JBCheckBox>
        row {
            smallImageCheckBox = checkBox("Show small image")
                .bindSelected(smallImageEnabled)
        }
        indent {
            row {
                comboBox(imageSettings)
                    .label("Icon:")
                    .bindItem(smallImage.toNullableProperty())
                textField()
                    .label("Text:")
                    .bindText(smallImageText)
                    .errorOnInput("Length must be between 2 and 128") { it.text.isNotEmpty() && it.text.length !in 2..128 }
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                    .errorOnApply("Length must be between 2 and 128") { it.isEnabled && it.text.length !in 2..128 }
            }
        }.enabledIf(smallImageCheckBox.selected)

        row {
            checkBox("Show elapsed time")
                .bindSelected(timestampEnabled)
        }

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
            val customApplicationIdCheckBox = checkBox("Custom Discord application id:")
                .bindSelected(state::customApplicationIdEnabled)
                .gap(RightGap.SMALL)
            textField()
                .bindText(state::customApplicationId)
                .enabledIf(customApplicationIdCheckBox.selected)
                .errorOnInput("Must be a valid id") { it.text.isNotEmpty() && it.text.toULongOrNull() == null }
                .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                .errorOnApply("Must be a valid id") { it.isEnabled && it.text.toULongOrNull() == null }
        }

        row {
            val idleTimeoutEnabled = checkBox("Hide Rich Presence when IDE is out of focus for")
                .bindSelected(state::focusTimeoutEnabled)
                .gap(RightGap.SMALL)
            intTextField(0..Int.MAX_VALUE)
                .bindIntText(state::focusTimeoutMinutes)
                .columns(COLUMNS_TINY)
                .gap(RightGap.SMALL)
                .enabledIf(idleTimeoutEnabled.selected)
            @Suppress("DialogTitleCapitalization")
            label("minutes")
        }

        row {
            label("IDE logo style:")
                .gap(RightGap.SMALL)
            comboBox(LogoStyleSetting.values().toList())
                .bindItem(state::logoStyle.toNullableProperty())
                .gap(RightGap.SMALL)
            contextHelp("Recently JetBrains has redesigned their IDE logos. You can switch between the old (classic) design and the new (modern) one.")
        }

        row {
            label("Default display mode:")
                .gap(RightGap.SMALL)
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
                    timestampEnabled = state::applicationTimestampEnabled,
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
                    timestampEnabled = state::projectTimestampEnabled,
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
                    timestampEnabled = state::fileTimestampEnabled,
                )
            }
        }
    }

    override fun apply() {
        val applicationIdBefore =
            if (discordSettingsComponent.state.customApplicationIdEnabled)
                discordSettingsComponent.state.customApplicationId
            else null

        if (validateAndApply()) {
            val applicationIdAfter =
                if (discordSettingsComponent.state.customApplicationIdEnabled)
                    discordSettingsComponent.state.customApplicationId
                else null

            val discordService = DiscordService.getInstance()

            // Reconnect if custom application id has been modified
            if (applicationIdBefore != applicationIdAfter) {
                discordService.reconnectBackground()
            } else {
                discordService.updateBackground()
            }
        }
    }
}