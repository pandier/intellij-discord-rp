package io.github.pandier.intellijdiscordrp.settings

import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.ActivityFactory
import io.github.pandier.intellijdiscordrp.settings.project.DiscordProjectSettings
import io.github.pandier.intellijdiscordrp.template.Templates
import io.github.pandier.intellijdiscordrp.template.node.TemplateNode

data class DiscordSettings(
    var autoReconnect: Boolean = true,
    var autoReconnectPeriod: Int = 20,
    var customApplicationIdEnabled: Boolean = false,
    var customApplicationId: String = "",
    var defaultDisplayMode: ActivityDisplayMode = ActivityDisplayMode.FILE,
    var focusTimeoutEnabled: Boolean = true,
    var focusTimeoutMinutes: Int = 20,
    var logoStyle: LogoStyleSetting = LogoStyleSetting.MODERN,
    var applicationMode: Mode = Mode(),
    var projectMode: Mode = Mode(
        details = "In {project_name}",
        timestampTarget = TimestampTargetSetting.PROJECT,
    ),
    var fileMode: Mode = Mode(
        details = "In {project_name}",
        state = "Editing {file_name}",
        largeIcon = Icon(
            type = IconType.FILE,
            tooltip = "{file_type}",
        ),
        smallIcon = Icon(
            type = IconType.APPLICATION,
            tooltip = "{app_name}",
        ),
        timestampTarget = TimestampTargetSetting.PROJECT,
    ),
) {
    val applicationId: Long
        get() = (if (customApplicationIdEnabled) customApplicationId.toLongOrNull() else null) ?: 1107202385799041054L

    val templates = Templates {
        child { applicationMode.templates }
        child { projectMode.templates }
        child { fileMode.templates }
    }

    data class Mode(
        var name: String = "{app_name}",
        var details: String = "",
        var state: String = "",
        var largeIcon: Icon = Icon(
            type = IconType.APPLICATION,
            tooltip = "{app_name}",
        ),
        var smallIcon: Icon = Icon(),
        var timestampTarget: TimestampTargetSetting = TimestampTargetSetting.APPLICATION,
    ) {
        val templates: Templates = Templates {
            child { largeIcon.templates }
            child { smallIcon.templates }
        }

        val nameNode: TemplateNode by templates.add { name }
        val detailsNode: TemplateNode by templates.add { details }
        val stateNode: TemplateNode by templates.add { state }
    }

    data class Icon(
        var type: IconType = IconType.HIDDEN,
        var tooltip: String = "",
        var altType: IconType = IconType.HIDDEN,
        var altTooltip: String = "",
    ) {
        val templates: Templates = Templates()

        val tooltipNode: TemplateNode by templates.add { tooltip }
        val altTooltipNode: TemplateNode by templates.add { altTooltip }
    }

    fun createActivityFactory(mode: ActivityDisplayMode, projectSettings: DiscordProjectSettings?): ActivityFactory? {
        val modeSettings = when (mode) {
            ActivityDisplayMode.HIDDEN -> return null
            ActivityDisplayMode.APPLICATION -> applicationMode
            ActivityDisplayMode.PROJECT -> projectMode
            ActivityDisplayMode.FILE -> fileMode
        }
        val projectSettings = projectSettings.takeIf { mode > ActivityDisplayMode.APPLICATION }
        return ActivityFactory(
            displayMode = mode,
            modeSettings = modeSettings,
            logoStyle = logoStyle,
            projectIcon = projectSettings?.icon,
            buttonTextNode = if (projectSettings?.buttonEnabled == true) projectSettings.buttonTextNode else null,
            buttonUrlNode = projectSettings?.buttonUrlNode,
        )
    }
}
