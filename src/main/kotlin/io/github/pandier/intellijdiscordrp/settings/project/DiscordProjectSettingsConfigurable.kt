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
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable
import io.github.pandier.intellijdiscordrp.settings.ui.errorOnInput
import io.github.pandier.intellijdiscordrp.util.urlRegex

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
            label("Display mode in project:")
                .gap(RightGap.SMALL)
            comboBox(listOf("Default", "Application", "Project", "File"))
                .bindItem({ state.displayMode?.toString() ?: "Default" }, { state.displayMode = it?.let(ActivityDisplayMode::byName) })
        }

        row {
            label("Icon:")
                .gap(RightGap.SMALL)
            // TODO: Check if text is too long/short
            textField()
                .bindText(state::icon)
                .columns(COLUMNS_LARGE)
                .also { it.component.emptyText.text = "Optional" }
            // TODO: Add context help
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
                    .label("Link:")
                    .bindText(state::buttonUrl)
                    .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
                    .validationOnApply { if ((!it.text.contains('{') || !it.text.contains('}')) && !urlRegex.matches(it.text)) warning("Not a valid URL") else null }
                    .gap(RightGap.SMALL)
                contextHelp("You can use variables in the URL field!")
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