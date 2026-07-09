package io.github.pandier.intellijdiscordrp.template.node

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

data class TemplateTextNode(
    val text: String,
) : TemplateNode {
    override fun resolve(context: ActivityContext, displayMode: ActivityDisplayMode): String {
        return text
    }

    override fun toString(): String {
        return "Text($text)"
    }
}
