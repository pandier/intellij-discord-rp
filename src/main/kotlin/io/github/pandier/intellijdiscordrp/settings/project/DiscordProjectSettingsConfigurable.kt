package io.github.pandier.intellijdiscordrp.settings.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.COLUMNS_LARGE
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.selected
import io.github.pandier.intellijdiscordrp.DiscordRichPresenceBundle
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.maxLength
import io.github.pandier.intellijdiscordrp.settings.ui.optional
import io.github.pandier.intellijdiscordrp.settings.ui.required
import io.github.pandier.intellijdiscordrp.settings.ui.validateUrlWithVariables

class DiscordProjectSettingsConfigurable(
    private val project: Project,
) : DslConfigurable(DiscordRichPresenceBundle.message("configurable.name.project")) {

    override fun createPanel(): DialogPanel = panel {
        val state = project.discordProjectSettingsComponent.state

        row {
            checkBox(DiscordRichPresenceBundle.message("settings.project.showRichPresence"))
                .bindSelected(state::showRichPresence)
        }

        row {
            comboBox(listOf("Default", "Application", "Project", "File"))
                .label(DiscordRichPresenceBundle.message("settings.project.displayMode"))
                .bindItem({ state.displayMode?.toString() ?: "Default" }, { state.displayMode = it?.let(ActivityDisplayMode::byName) })
        }

        row {
            textField()
                .label(DiscordRichPresenceBundle.message("settings.project.icon"))
                .bindText(state::icon)
                .columns(COLUMNS_LARGE)
                .validateUrlWithVariables()
                .maxLength(256)
                .optional()
                .gap(RightGap.SMALL)
            contextHelp(DiscordRichPresenceBundle.message("settings.project.icon.context"))
        }

        lateinit var buttonCheckBox: Cell<JBCheckBox>
        row {
            buttonCheckBox = checkBox(DiscordRichPresenceBundle.message("settings.project.button"))
                .bindSelected(state::buttonEnabled)
                .gap(RightGap.SMALL)
            contextHelp(DiscordRichPresenceBundle.message("settings.project.button.context"))
        }
        indent {
            row {
                textField()
                    .label(DiscordRichPresenceBundle.message("settings.project.button.text"))
                    .bindText(state::buttonText)
                    .maxLength(31)
                    .required()
                textField()
                    .label(DiscordRichPresenceBundle.message("settings.project.button.url"))
                    .bindText(state::buttonUrl)
                    .gap(RightGap.SMALL)
                    .validateUrlWithVariables()
                    .required()
                contextHelp(DiscordRichPresenceBundle.message("settings.project.button.url.context"))
            }
        }.enabledIf(buttonCheckBox.selected)
    }

    override fun apply() {
        if (validateAndApply()) {
            val discordService = DiscordService.getInstance()
            discordService.updateBackground()
        }
    }
}