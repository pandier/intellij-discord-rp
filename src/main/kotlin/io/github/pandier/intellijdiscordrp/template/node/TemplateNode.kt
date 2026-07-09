package io.github.pandier.intellijdiscordrp.template.node

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

interface TemplateNode {
    fun resolve(context: ActivityContext, displayMode: ActivityDisplayMode): String
}
