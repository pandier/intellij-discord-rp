package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.template.node.TemplateCompositeNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateTextNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateVariableNode

const val TEMPLATE_ALLOWED_CHARS: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_"

class TemplateParser(val string: String) {
    private var cursor = 0

    companion object {
        fun parse(string: String): TemplateNode {
            return TemplateParser(string).parse()
        }
    }

    fun parse(): TemplateNode {
        val nodes = mutableListOf<TemplateNode>()

        val text = StringBuilder()

        outer@ while (true) {
            text.append(readUntil('{'))

            val start = cursor

            if (read() == null) break // eof

            // {{ = {
            if (peek() == '{') {
                read()
                text.append('{')
                continue
            }

            while (true) {
                val ch = read()
                if (ch == null) { // eof
                    text.append(string.substring(start))
                    break@outer
                } else if (ch == '}') {
                    break
                } else if (ch !in TEMPLATE_ALLOWED_CHARS) {
                    text.append(string.substring(start, cursor))
                    continue@outer
                }
            }

            val name = string.substring(start + 1, cursor - 1)
            val variable = TemplateVariables.getByName(name) ?: continue

            if (text.isNotEmpty()) {
                nodes.add(TemplateTextNode(text.toString().replace("}}", "}")))
                text.clear()
            }
            nodes.add(TemplateVariableNode(variable))
        }

        if (text.isNotEmpty()) {
            nodes.add(TemplateTextNode(text.toString().replace("}}", "}")))
        }

        if (nodes.size == 1) return nodes[0]
        return TemplateCompositeNode(nodes)
    }

    private fun readUntil(char: Char): String {
        var end = string.indexOf(char, cursor)
        if (end < 0) end = string.length
        val result = string.substring(cursor, end)
        cursor = end
        return result
    }

    private fun read(): Char? {
        if (cursor >= string.length) return null
        return string[cursor++]
    }

    private fun peek(): Char? {
        if (cursor >= string.length) return null
        return string[cursor]
    }
}
