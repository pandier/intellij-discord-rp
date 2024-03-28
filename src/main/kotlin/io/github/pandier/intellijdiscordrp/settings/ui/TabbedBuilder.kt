package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.layout.ComponentPredicate

fun Panel.tabbed(init: TabbedBuilder.() -> Unit) =
    TabbedBuilder().also(init).finish(this)

class TabbedBuilder {
    private val tabTitles: MutableList<String> = mutableListOf()
    private val tabBuilders: MutableList<Panel.() -> Unit> = mutableListOf()

    fun tab(title: String, init: Panel.() -> Unit) {
        tabTitles.add(title)
        tabBuilders.add(init)
    }

    fun finish(panel: Panel) {
        lateinit var tabbedPane: Cell<JBTabbedPane>

        // Add a tabbed pane header to the panel
        panel.row {
            @Suppress("UnstableApiUsage")
            tabbedPane = tabbedPaneHeader(tabTitles)
        }

        // Put the tab contents as seperate indents visible only if the tab is selected
        for ((index, builder) in tabBuilders.withIndex()) {
            panel.indent(builder).visibleIf(TabPredicate(tabbedPane.component, index))
        }
    }
}

private class TabPredicate(
    private val tabbedPane: JBTabbedPane,
    private val index: Int
) : ComponentPredicate() {
    override fun addListener(listener: (Boolean) -> Unit) =
        tabbedPane.addChangeListener { listener(invoke()) }

    override fun invoke(): Boolean =
        tabbedPane.selectedIndex == index
}
