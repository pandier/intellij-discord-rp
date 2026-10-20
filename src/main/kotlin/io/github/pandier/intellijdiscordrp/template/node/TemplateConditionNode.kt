package io.github.pandier.intellijdiscordrp.template.node

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode
import io.github.pandier.intellijdiscordrp.template.TemplateVariable

data class TemplateConditionNode(
    val variable: TemplateVariable,
    val positive: Boolean,
    val trueNode: TemplateNode?,
    val falseNode: TemplateNode?,
) : TemplateNode {
    override fun resolve(context: ActivityContext, displayMode: ActivityDisplayMode): String {
        if (!variable.supports(displayMode)) return ""
        val isPositive = variable.supports(displayMode) && variable.getValue(context)?.isNotEmpty() == true
        return if (this.positive == isPositive) {
            trueNode?.resolve(context, displayMode) ?: ""
        } else {
            falseNode?.resolve(context, displayMode) ?: ""
        }
    }

    override fun toString(): String {
        return "Condition(${variable.name})" // TODO
    }
}
