package io.github.pandier.intellijdiscordrp.template.node

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.activity.ActivityVariable

data class TemplateVariableNode(
    val variable: ActivityVariable,
) : TemplateNode {
    override fun resolve(context: ActivityContext, displayMode: ActivityDisplayMode): String {
        if (!variable.supports(displayMode)) return ""
        return variable.getValue(context) ?: ""
    }

    override fun toString(): String {
        return "Variable(${variable.name})"
    }
}
