package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.template.node.TemplateNode
import kotlin.reflect.KProperty

fun Templates(block: Templates.() -> Unit): Templates {
    return Templates().apply(block)
}

class Templates {
    private val delegates: MutableList<Delegate> = mutableListOf()
    private val children: MutableList<() -> Templates> = mutableListOf()

    fun add(input: () -> String): Delegate {
        return Delegate(input).also { delegates.add(it) }
    }

    fun child(child: () -> Templates) {
        children.add(child)
    }

    fun invalidate() {
        for (delegate in delegates) {
            delegate.invalidate()
        }
        for (child in children) {
            child().invalidate()
        }
    }

    class Delegate(private val input: () -> String) {
        private var node: TemplateNode? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>): TemplateNode {
            return node ?: TemplateParser.parse(input()).also { node = it }
        }

        fun invalidate() {
            node = null
        }
    }
}