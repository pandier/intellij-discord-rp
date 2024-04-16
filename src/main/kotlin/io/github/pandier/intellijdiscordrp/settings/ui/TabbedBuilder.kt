package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaTabbedPaneUI
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.layout.ComponentPredicate
import java.awt.Dimension
import javax.swing.JPanel

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
        val tabbedHeader = TabbedHeader()
        tabTitles.forEach(tabbedHeader::addTab)

        // Add a tabbed pane header to the panel
        panel.row {
            cell(tabbedHeader)
        }

        // Put the tab contents as seperate indents visible only if the tab is selected
        for ((index, builder) in tabBuilders.withIndex()) {
            panel.indent(builder).visibleIf(TabPredicate(tabbedHeader, index))
        }
    }
}

private class TabbedHeader : JBTabbedPane() {
    override fun updateUI() {
        setUI(UI())
    }

    override fun getPreferredSize(): Dimension {
        val insets = insets
        val ui = ui as UI
        return Dimension(
            ui.getWidth() + insets.right + insets.left,
            ui.getHeight() + insets.top + insets.bottom,
        )
    }

    override fun getMinimumSize(): Dimension =
        preferredSize

    fun addTab(title: String) =
        addTab(title, JPanel())

    class UI : DarculaTabbedPaneUI() {
        fun getWidth(): Int {
            val metrics = tabPane.getFontMetrics(tabPane.font)
            return (0 until tabPane.tabCount)
                .sumOf { calculateTabWidth(tabPane.tabPlacement, it, metrics) }
        }

        fun getHeight(): Int =
            calculateMaxTabHeight(tabPane.tabPlacement)
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
