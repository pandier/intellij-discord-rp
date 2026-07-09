package io.github.pandier.intellijdiscordrp.template.node

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

data class TemplateCompositeNode(
    val nodes: List<TemplateNode>,
) : TemplateNode {
    override fun resolve(context: ActivityContext, displayMode: ActivityDisplayMode): String {
        return nodes.joinToString("") { it.resolve(context, displayMode) }
    }

    override fun toString(): String {
        return "Composite(" + nodes.joinToString(", ") { it.toString() } + ")"
    }
}
