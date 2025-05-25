package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.observable.util.transform
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.ActionLink
import com.intellij.ui.dsl.builder.*
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.currentActivityApplicationType
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.TabbedBuilder
import io.github.pandier.intellijdiscordrp.settings.ui.errorOnInput
import io.github.pandier.intellijdiscordrp.settings.ui.tabbed
import org.jetbrains.annotations.Nls
import kotlin.reflect.KMutableProperty0

/**
 * Creates a row where the user can configure a specific icon for the activity.
 */
private fun Panel.iconRow(
    @Nls label: String,
    icons: List<IconSetting>,
    icon: KMutableProperty0<IconSetting>,
    text: KMutableProperty0<String>,
    altIcon: KMutableProperty0<IconSetting>,
    altText: KMutableProperty0<String>,
) {
    lateinit var iconProperty: AtomicProperty<IconSetting>

    val icons = icons.toMutableList()
    icons.add(0, IconSetting.HIDDEN)

    row(label) {
        comboBox(icons)
            .bindItem(icon.toNullableProperty())
            .onChanged { iconProperty.set(it.item) }
            .apply { iconProperty = AtomicProperty(component.item) }

        textField()
            .label("Tooltip:")
            .bindText(text)
            .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
            .errorOnApply("Text cannot be longer than 128 characters") { it.isEnabled && it.text.length > 128 }
            .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
            .enabledIf(iconProperty.transform { it != IconSetting.HIDDEN })
    }

    if (!icons.contains(IconSetting.PROJECT))
        return

    indent {
        row {
            lateinit var altIconProperty: AtomicProperty<IconSetting>
            val configurations = icons.toMutableList()
            configurations.remove(IconSetting.PROJECT)

            comboBox(configurations)
                .label("Alternative:")
                .bindItem(altIcon.toNullableProperty())
                .gap(RightGap.SMALL)
                .onChanged { altIconProperty.set(it.item); }
                .apply { altIconProperty = AtomicProperty(component.item) }

            contextHelp("This icon will be shown when the project icon is not available.")

            textField()
                .label("Tooltip:")
                .bindText(altText)
                .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .errorOnApply("Text cannot be longer than 128 characters") { it.isEnabled && it.text.length > 128 }
                .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                .enabledIf(altIconProperty.transform { it != IconSetting.HIDDEN })
        }
    }.visibleIf(iconProperty.transform { it == IconSetting.PROJECT })
}

private fun TabbedBuilder.displayModeTab(
    mode: DiscordSettings.Mode,
    displayMode: ActivityDisplayMode,
    icons: List<IconSetting>,
    timestampTargets: List<TimestampTargetSetting>,
) {
    tab(displayMode.toString()) {
        row("Details:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(mode::details)
                .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .errorOnApply("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .also { it.component.emptyText.text = "Optional" }
        }
        row("State:") {
            textField()
                .columns(COLUMNS_LARGE)
                .bindText(mode::state)
                .errorOnInput("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .errorOnApply("Text cannot be longer than 128 characters") { it.text.isNotEmpty() && it.text.length > 128 }
                .also { it.component.emptyText.text = "Optional" }
        }

        iconRow("Large icon:", icons, mode::largeIcon, mode::largeIconTooltip, mode::largeIconAlt, mode::largeIconAltTooltip)
        iconRow("Small icon:", icons, mode::smallIcon, mode::smallIconTooltip, mode::smallIconAlt, mode::smallIconAltTooltip)

        row {
            checkBox("Show elapsed time in")
                .bindSelected(mode::timestampEnabled)
                .gap(RightGap.SMALL)
            comboBox(timestampTargets)
                .bindItem(mode::timestampTarget.toNullableProperty())
        }

        row {
            val lines = displayMode.variables.map { variable ->
                val availabilityError = variable.availabilityCheck()
                if (availabilityError == null) {
                    "<code>$variable</code> - ${variable.description}"
                } else {
                    "<s><code>$variable</code> - ${variable.description}</s> ($availabilityError)"
                }
            }
            comment(lines.joinToString("<br/>"))
        }
    }
}

class DiscordSettingsConfigurable : DslConfigurable("Discord Rich Presence") {

    override fun createPanel(): DialogPanel = panel {
        val state = discordSettingsComponent.state

        row {
            cell(ActionLink("Reset to defaults") {
                val result = Messages.showYesNoDialog(
                    "Are you sure you want to reset all settings to their default values?",
                    "Reset Settings",
                    Messages.getQuestionIcon()
                )
                if (result != Messages.YES) return@ActionLink
                discordSettingsComponent.loadState(DiscordSettings())
                reset()
            }.apply {
                toolTipText = "Reset all settings to their default values"
            }).align(AlignX.RIGHT)
        }

        row {
            checkBox("Try reconnecting on every activity update if not connected")
                .bindSelected(state::reconnectOnUpdate)
                .gap(RightGap.SMALL)
            contextHelp("Reconnection process runs in the background and in most cases shouldn't affect performance.")
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
        }

        group("Display") {
            tabbed {
                displayModeTab(
                    mode = state.applicationMode,
                    displayMode = ActivityDisplayMode.APPLICATION,
                    icons = listOf(IconSetting.APPLICATION),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION),
                )

                displayModeTab(
                    mode = state.projectMode,
                    displayMode = ActivityDisplayMode.PROJECT,
                    icons = listOf(IconSetting.APPLICATION, IconSetting.PROJECT),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT),
                )

                displayModeTab(
                    mode = state.fileMode,
                    displayMode = ActivityDisplayMode.FILE,
                    icons = listOf(IconSetting.APPLICATION, IconSetting.PROJECT, IconSetting.FILE),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT, TimestampTargetSetting.FILE),
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