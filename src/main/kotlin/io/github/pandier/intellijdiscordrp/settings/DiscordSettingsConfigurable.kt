package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.TabbedBuilder
import io.github.pandier.intellijdiscordrp.settings.ui.errorOnInput
import io.github.pandier.intellijdiscordrp.settings.ui.tabbed
import kotlin.reflect.KMutableProperty0

private fun TabbedBuilder.displayModeTab(
    displayMode: ActivityDisplayMode,
    imageSettings: List<ImageSetting>,
    timestampTargetSettings: List<TimestampTargetSetting>,
    details: KMutableProperty0<String>,
    state: KMutableProperty0<String>,
    largeImage: KMutableProperty0<ImageSetting>,
    largeImageEnabled: KMutableProperty0<Boolean>,
    largeImageText: KMutableProperty0<String>,
    smallImage: KMutableProperty0<ImageSetting>,
    smallImageEnabled: KMutableProperty0<Boolean>,
    smallImageText: KMutableProperty0<String>,
    repoButtonEnabled: KMutableProperty0<Boolean>?,
    repoButtonText: KMutableProperty0<String>?,
    timestampEnabled: KMutableProperty0<Boolean>,
    timestampTarget: KMutableProperty0<TimestampTargetSetting>?,
) {
    tab(displayMode.toString()) {
        row("Details:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(details)
                .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .errorOnApply("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .also { it.component.emptyText.text = "Optional" }
        }
        row("State:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(state)
                .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .errorOnApply("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
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
                    .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                    .errorOnApply("Text cannot be longer than 128 characters") { it.isEnabled && it.text.length > 128 }
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
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
                    .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                    .errorOnApply("Text cannot be longer than 128 characters") { it.isEnabled && it.text.length > 128 }
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
            }
        }.enabledIf(smallImageCheckBox.selected)

        if (repoButtonEnabled != null && repoButtonText != null) {
            lateinit var repoButtonCheckBox: Cell<JBCheckBox>
            row {
                repoButtonCheckBox = checkBox("Show repository button")
                    .bindSelected(repoButtonEnabled)
                    .gap(RightGap.SMALL)
                contextHelp("Adds a button to the Rich Presence that links to the project's Git repository. " +
                        "<b>Due to a bug in the Discord client, the button is visible to everyone except you.</b>")
            }
            indent {
                row {
                    textField()
                        .label("Text:")
                        .bindText(repoButtonText)
                        .columns(COLUMNS_LARGE)
                        .errorOnInput("Text cannot be longer than 31 characters") { it.text.isNotEmpty() && it.text.length > 31 }
                        .errorOnApply("Text cannot be longer than 31 characters") { it.isEnabled && it.text.length > 31 }
                        .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                }
            }.enabledIf(repoButtonCheckBox.selected)
        }

        row {
            checkBox("Show elapsed time in")
                .bindSelected(timestampEnabled)
                .gap(RightGap.SMALL)
            val timestampTargetComboBox = comboBox(timestampTargetSettings)
            if (timestampTarget != null)
                timestampTargetComboBox.bindItem(timestampTarget.toNullableProperty())
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
            checkBox("Try reconnecting on every activity update if not connected")
                .bindSelected(state::reconnectOnUpdate)
                .gap(RightGap.SMALL)
            contextHelp("Reconnection process runs in the background and in most cases shouldn't affect performance.")
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

        // Show full application name option only when available
        if (currentActivityApplicationType.fullNameDiscordApplicationId != null) {
            row {
                checkBox("Show full application name with edition in title")
                    .bindSelected(state::showFullApplicationName)
                    .gap(RightGap.SMALL)
                contextHelp("Title is located in the Rich Presence at the top or in your Discord status. " +
                        "Use a custom Discord application id if you want to fully adjust the title.")
            }
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
                    timestampTargetSettings = listOf(TimestampTargetSetting.APPLICATION),
                    details = state::applicationDetails,
                    state = state::applicationState,
                    largeImage = state::applicationLargeImage,
                    largeImageEnabled = state::applicationLargeImageEnabled,
                    largeImageText = state::applicationLargeImageText,
                    smallImage = state::applicationSmallImage,
                    smallImageEnabled = state::applicationSmallImageEnabled,
                    smallImageText = state::applicationSmallImageText,
                    repoButtonEnabled = null,
                    repoButtonText = null,
                    timestampEnabled = state::applicationTimestampEnabled,
                    timestampTarget = null,
                )

                displayModeTab(
                    displayMode = ActivityDisplayMode.PROJECT,
                    imageSettings = listOf(ImageSetting.APPLICATION),
                    timestampTargetSettings = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT),
                    details = state::projectDetails,
                    state = state::projectState,
                    largeImage = state::projectLargeImage,
                    largeImageEnabled = state::projectLargeImageEnabled,
                    largeImageText = state::projectLargeImageText,
                    smallImage = state::projectSmallImage,
                    smallImageEnabled = state::projectSmallImageEnabled,
                    smallImageText = state::projectSmallImageText,
                    repoButtonEnabled = state::projectRepoButtonEnabled,
                    repoButtonText = state::projectRepoButtonText,
                    timestampEnabled = state::projectTimestampEnabled,
                    timestampTarget = state::projectTimestampTarget,
                )

                displayModeTab(
                    displayMode = ActivityDisplayMode.FILE,
                    imageSettings = listOf(ImageSetting.APPLICATION, ImageSetting.FILE),
                    timestampTargetSettings = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT, TimestampTargetSetting.FILE),
                    details = state::fileDetails,
                    state = state::fileState,
                    largeImage = state::fileLargeImage,
                    largeImageEnabled = state::fileLargeImageEnabled,
                    largeImageText = state::fileLargeImageText,
                    smallImage = state::fileSmallImage,
                    smallImageEnabled = state::fileSmallImageEnabled,
                    smallImageText = state::fileSmallImageText,
                    repoButtonEnabled = state::fileRepoButtonEnabled,
                    repoButtonText = state::fileRepoButtonText,
                    timestampEnabled = state::fileTimestampEnabled,
                    timestampTarget = state::fileTimestampTarget,
                )
            }
        }
    }

    override fun apply() {
        val applicationIdBefore =
            if (discordSettingsComponent.state.customApplicationIdEnabled)
                discordSettingsComponent.state.customApplicationId
            else null
        val showFullApplicationNameBefore = discordSettingsComponent.state.showFullApplicationName

        if (validateAndApply()) {
            val applicationIdAfter =
                if (discordSettingsComponent.state.customApplicationIdEnabled)
                    discordSettingsComponent.state.customApplicationId
                else null
            val showFullApplicationNameAfter = discordSettingsComponent.state.showFullApplicationName

            val discordService = DiscordService.getInstance()

            // Reconnect if custom application id or show full application has been modified
            // because these changes require it
            if (applicationIdBefore != applicationIdAfter || showFullApplicationNameBefore != showFullApplicationNameAfter) {
                discordService.reconnectBackground()
            } else {
                discordService.updateBackground()
            }
        }
    }
}