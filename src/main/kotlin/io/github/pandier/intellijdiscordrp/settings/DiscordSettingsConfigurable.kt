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
import io.github.pandier.intellijdiscordrp.settings.ui.map
import io.github.pandier.intellijdiscordrp.settings.ui.maxLength
import io.github.pandier.intellijdiscordrp.settings.ui.optional
import io.github.pandier.intellijdiscordrp.settings.ui.required
import io.github.pandier.intellijdiscordrp.settings.ui.tabbed
import org.jetbrains.annotations.Nls
import kotlin.reflect.KMutableProperty0

/**
 * Creates a row where the user can configure a specific icon for the activity.
 */
private fun Panel.iconRow(
    @Nls label: String,
    icons: List<IconType>,
    icon: MutableProperty<DiscordSettings.Icon>,
) {
    lateinit var iconProperty: AtomicProperty<IconType>

    val icons = icons.toMutableList()
    icons.add(0, IconType.HIDDEN)

    row(label) {
        comboBox(icons)
            .bindItem(icon.map { it::type }.toNullableProperty())
            .onChanged { iconProperty.set(it.item) }
            .apply { iconProperty = AtomicProperty(component.item) }

        textField()
            .label("Tooltip:")
            .bindText(icon.map { it::tooltip })
            .maxLength(128)
            .required()
            .enabledIf(iconProperty.transform { it != IconType.HIDDEN })
    }

    if (!icons.contains(IconType.PROJECT))
        return

    indent {
        row {
            lateinit var altIconProperty: AtomicProperty<IconType>
            val configurations = icons.toMutableList()
            configurations.remove(IconType.PROJECT)

            comboBox(configurations)
                .label("Alternative:")
                .bindItem(icon.map { it::altType }.toNullableProperty())
                .gap(RightGap.SMALL)
                .onChanged { altIconProperty.set(it.item); }
                .apply { altIconProperty = AtomicProperty(component.item) }

            contextHelp("This icon will be shown when the project icon is not available.")

            textField()
                .label("Tooltip:")
                .bindText(icon.map { it::altTooltip })
                .maxLength(128)
                .required()
                .enabledIf(altIconProperty.transform { it != IconType.HIDDEN })
        }
    }.visibleIf(iconProperty.transform { it == IconType.PROJECT })
}

private fun TabbedBuilder.displayModeTab(
    mode: KMutableProperty0<DiscordSettings.Mode>,
    displayMode: ActivityDisplayMode,
    icons: List<IconType>,
    timestampTargets: List<TimestampTargetSetting>,
) {
    tab(displayMode.toString()) {
        row("Details:") {
            textField()
                .bindText(mode.map { it::details })
                .columns(COLUMNS_LARGE)
                .maxLength(128)
                .optional()
        }
        row("State:") {
            textField()
                .bindText(mode.map { it::state })
                .columns(COLUMNS_LARGE)
                .maxLength(128)
                .optional()
        }

        iconRow("Large icon:", icons, mode.map { it::largeIcon })
        iconRow("Small icon:", icons, mode.map { it::smallIcon })

        row {
            checkBox("Show elapsed time in")
                .bindSelected(mode.map { it::timestampEnabled })
                .gap(RightGap.SMALL)
            comboBox(timestampTargets)
                .bindItem(mode.map { it::timestampTarget }.toNullableProperty())
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
        val state = discordSettingsComponent.settings

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
                .errorOnInput("Not a valid id") { it.text.isNotEmpty() && it.text.toULongOrNull() == null }
                .errorOnApply("Not a valid id") { it.isEnabled && it.text.toULongOrNull() == null }
                .required()
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
                    mode = state::applicationMode,
                    displayMode = ActivityDisplayMode.APPLICATION,
                    icons = listOf(IconType.APPLICATION),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION),
                )

                displayModeTab(
                    mode = state::projectMode,
                    displayMode = ActivityDisplayMode.PROJECT,
                    icons = listOf(IconType.APPLICATION, IconType.PROJECT),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT),
                )

                displayModeTab(
                    mode = state::fileMode,
                    displayMode = ActivityDisplayMode.FILE,
                    icons = listOf(IconType.APPLICATION, IconType.PROJECT, IconType.FILE),
                    timestampTargets = listOf(TimestampTargetSetting.APPLICATION, TimestampTargetSetting.PROJECT, TimestampTargetSetting.FILE),
                )
            }
        }

        row {
            cell(ActionLink("Reset to defaults") {
                val result = Messages.showYesNoDialog(
                    "Are you sure you want to reset all settings to their default values?",
                    "Reset Settings",
                    Messages.getQuestionIcon()
                )
                if (result != Messages.YES) return@ActionLink
                discordSettingsComponent.setSettings(DiscordSettings())
                reset()
            }.apply {
                toolTipText = "Reset all settings to their default values"
            })
        }
    }

    override fun apply() {
        val applicationIdBefore =
            if (discordSettingsComponent.settings.customApplicationIdEnabled)
                discordSettingsComponent.settings.customApplicationId
            else null
        val showFullApplicationNameBefore = discordSettingsComponent.settings.showFullApplicationName

        if (validateAndApply()) {
            val applicationIdAfter =
                if (discordSettingsComponent.settings.customApplicationIdEnabled)
                    discordSettingsComponent.settings.customApplicationId
                else null
            val showFullApplicationNameAfter = discordSettingsComponent.settings.showFullApplicationName

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