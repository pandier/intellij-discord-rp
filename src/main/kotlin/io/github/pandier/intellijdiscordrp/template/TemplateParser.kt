package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.template.node.TemplateCompositeNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateTextNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateVariableNode

object TemplateParser {
    class Input(val string: String) {
        var index = 0

        fun readUntil(char: Char): String {
            var end = string.indexOf(char, index)
            if (end < 0) end = string.length
            val result = string.substring(index, end)
            index = end
            return result
        }

        fun readChar(): Char? {
            if (!hasRemaining()) return null
            return string[index++]
        }

        fun peekChar(): Char? {
            if (!hasRemaining()) return null
            return string[index]
        }

        fun hasRemaining(): Boolean {
            return index < string.length
        }
    }

    fun parse(string: String): TemplateNode {
        return parse(Input(string))
    }

    fun parse(input: Input): TemplateNode {
        val nodes: MutableList<TemplateNode> = mutableListOf()
        val text: StringBuilder = StringBuilder()

        while (input.hasRemaining()) {
            text.append(input.readUntil('{'))

            val start = input.index

            input.readChar()

            if (input.peekChar() == '{') {
                input.readChar()
                text.append('{')
                continue
            }

            val variableName = input.readUntil('}')

            if (input.readChar() != '}') {
                text.append(input.string.substring(start, input.index))
                continue
            }

            val variable = TemplateVariables.getByName(variableName) ?: continue

            if (!text.isEmpty()) {
                nodes.add(TemplateTextNode(text.toString().replace("}}", "}")))
            }

            nodes.add(TemplateVariableNode(variable))
            text.clear()
        }

        if (!text.isEmpty()) {
            nodes.add(TemplateTextNode(text.toString().replace("}}", "}")))
        }

        if (nodes.size == 1) {
            return nodes.first()
        }
        return TemplateCompositeNode(nodes)
    }
}
