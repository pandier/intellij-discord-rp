package io.github.pandier.intellijdiscordrp.settings.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.service.DiscordService
import io.github.pandier.intellijdiscordrp.settings.ui.DslConfigurable

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
    }

    override fun apply() {
        if (validateAndApply()) {
            val discordService = DiscordService.getInstance()
            discordService.updateBackground()
        }
    }
}