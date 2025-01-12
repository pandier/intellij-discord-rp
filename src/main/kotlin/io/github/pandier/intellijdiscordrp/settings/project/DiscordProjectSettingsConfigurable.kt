package io.github.pandier.intellijdiscordrp.settings.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.selected
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.errorOnInput

class DiscordProjectSettingsConfigurable(
    private val project: Project,
) : DslConfigurable("Project") {

    override fun createPanel(): DialogPanel = panel {
        val state = project.discordProjectSettingsComponent.state

        row {
            checkBox("Show rich presence in project")
                .bindSelected(state::showRichPresence)
        }

        row {
            checkBox("Show repository button in project")
                .bindSelected(state::showRepoButton)
        }

        row {
            label("Display mode in project:")
                .gap(RightGap.SMALL)
            comboBox(listOf("Default", "Application", "Project", "File"))
                .bindItem({ state.displayMode?.toString() ?: "Default" }, { state.displayMode = it?.let(ActivityDisplayMode::byName) })
        }

        lateinit var buttonCheckBox: Cell<JBCheckBox>
        row {
            buttonCheckBox = checkBox("Show custom button in project")
                .bindSelected(state::buttonEnabled)
        }
        indent {
            row {
                textField()
                    .label("Text:")
                    .bindText(state::buttonText)
                    .errorOnInput("Text cannot be longer than 31 characters") { it.text.isNotEmpty() && it.text.length > 31 }
                    .errorOnApply("Text cannot be longer than 31 characters") { it.isEnabled && it.text.length > 31 }
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                textField()
                    .label("URL:")
                    .bindText(state::buttonUrl)
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
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