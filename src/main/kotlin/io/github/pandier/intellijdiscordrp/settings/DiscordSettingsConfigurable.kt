package io.github.pandier.intellijdiscordrp.settings

import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.observable.util.transform
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.ActionLink
import com.intellij.ui.dsl.builder.*
import io.github.pandier.intellijdiscordrp.DiscordRichPresenceBundle
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
            .label(DiscordRichPresenceBundle.message("settings.display.icon.tooltip"))
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
                .label(DiscordRichPresenceBundle.message("settings.display.icon.alternative"))
                .bindItem(icon.map { it::altType }.toNullableProperty())
                .gap(RightGap.SMALL)
                .onChanged { altIconProperty.set(it.item); }
                .apply { altIconProperty = AtomicProperty(component.item) }

            contextHelp(DiscordRichPresenceBundle.message("settings.display.icon.alternative.context"))

            textField()
                .label(DiscordRichPresenceBundle.message("settings.display.icon.tooltip"))
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
        row(DiscordRichPresenceBundle.message("settings.display.details")) {
            textField()
                .bindText(mode.map { it::details })
                .columns(COLUMNS_LARGE)
                .maxLength(128)
                .optional()
        }
        row(DiscordRichPresenceBundle.message("settings.display.state")) {
            textField()
                .bindText(mode.map { it::state })
                .columns(COLUMNS_LARGE)
                .maxLength(128)
                .optional()
        }

        iconRow(DiscordRichPresenceBundle.message("settings.display.largeIcon"), icons, mode.map { it::largeIcon })
        iconRow(DiscordRichPresenceBundle.message("settings.display.smallIcon"), icons, mode.map { it::smallIcon })

        row {
            checkBox(DiscordRichPresenceBundle.message("settings.display.elapsedTime"))
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

class DiscordSettingsConfigurable : DslConfigurable(DiscordRichPresenceBundle.message("configurable.name.discordRichPresence")) {

    override fun createPanel(): DialogPanel = panel {
        val state = discordSettingsComponent.settings

        row {
            checkBox(DiscordRichPresenceBundle.message("settings.reconnectOnUpdate"))
                .bindSelected(state::reconnectOnUpdate)
                .gap(RightGap.SMALL)
            contextHelp(DiscordRichPresenceBundle.message("settings.reconnectOnUpdate.context"))
        }

        // Show full application name option only when available
        if (currentActivityApplicationType.fullNameDiscordApplicationId != null) {
            row {
                checkBox(DiscordRichPresenceBundle.message("settings.showFullApplicationName"))
                    .bindSelected(state::showFullApplicationName)
                    .gap(RightGap.SMALL)
                contextHelp(DiscordRichPresenceBundle.message("settings.showFullApplicationName.context"))
            }
        }

        row {
            val customApplicationIdCheckBox = checkBox(DiscordRichPresenceBundle.message("settings.customApplicationId"))
                .bindSelected(state::customApplicationIdEnabled)
                .gap(RightGap.SMALL)
            textField()
                .bindText(state::customApplicationId)
                .enabledIf(customApplicationIdCheckBox.selected)
                .errorOnInput(DiscordRichPresenceBundle.message("dialog.validation.invalidId")) { it.text.isNotEmpty() && it.text.toULongOrNull() == null }
                .errorOnApply(DiscordRichPresenceBundle.message("dialog.validation.invalidId")) { it.isEnabled && it.text.toULongOrNull() == null }
                .required()
        }

        row {
            val idleTimeoutEnabled = checkBox(DiscordRichPresenceBundle.message("settings.focusTimeout"))
                .bindSelected(state::focusTimeoutEnabled)
                .gap(RightGap.SMALL)
            intTextField(0..Int.MAX_VALUE)
                .bindIntText(state::focusTimeoutMinutes)
                .columns(COLUMNS_TINY)
                .gap(RightGap.SMALL)
                .enabledIf(idleTimeoutEnabled.selected)
            @Suppress("DialogTitleCapitalization")
            label(DiscordRichPresenceBundle.message("settings.focusTimeout.minutes"))
        }

        row {
            label(DiscordRichPresenceBundle.message("settings.logoStyle"))
                .gap(RightGap.SMALL)
            comboBox(LogoStyleSetting.values().toList())
                .bindItem(state::logoStyle.toNullableProperty())
                .gap(RightGap.SMALL)
            contextHelp(DiscordRichPresenceBundle.message("settings.logoStyle.context"))
        }

        row {
            label(DiscordRichPresenceBundle.message("settings.defaultDisplayMode"))
                .gap(RightGap.SMALL)
            comboBox(ActivityDisplayMode.values().toList())
                .bindItem(state::defaultDisplayMode.toNullableProperty())
        }

        group(DiscordRichPresenceBundle.message("settings.group.display")) {
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
            cell(ActionLink(DiscordRichPresenceBundle.message("settings.reset")) {
                val result = Messages.showYesNoDialog(
                    DiscordRichPresenceBundle.message("dialog.resetSettings.content"),
                    DiscordRichPresenceBundle.message("dialog.resetSettings.title"),
                    Messages.getQuestionIcon()
                )
                if (result != Messages.YES) return@ActionLink
                discordSettingsComponent.setSettings(DiscordSettings())
                reset()
            }.apply {
                toolTipText = DiscordRichPresenceBundle.message("settings.reset.tooltip")
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