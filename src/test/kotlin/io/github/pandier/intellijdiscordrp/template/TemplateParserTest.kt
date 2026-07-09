package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.template.node.TemplateCompositeNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateTextNode
import io.github.pandier.intellijdiscordrp.template.node.TemplateVariableNode
import org.junit.jupiter.api.assertAll
import kotlin.test.Test
import kotlin.test.assertEquals

class TemplateParserTest {

    @Test
    fun `single text node`() {
        assertEquals(TemplateTextNode("example text"), TemplateParser.parse("example text"))
    }

    @Test
    fun `single variable node`() {
        assertEquals(TemplateVariableNode(TemplateVariables.projectName), TemplateParser.parse("{project_name}"))
    }

    @Test
    fun `empty composite node`() {
        assertEquals(TemplateCompositeNode(listOf()), TemplateParser.parse(""))
    }

    @Test
    fun `composite node`() {
        assertEquals(
            TemplateCompositeNode(listOf(
                TemplateTextNode("Working on "),
                TemplateVariableNode(TemplateVariables.projectName),
                TemplateTextNode(" project"),
            )),
            TemplateParser.parse("Working on {project_name} project")
        )
    }

    @Test
    fun `composite node flipped`() {
        assertEquals(
            TemplateCompositeNode(listOf(
                TemplateVariableNode(TemplateVariables.fileName),
                TemplateTextNode(" - "),
                TemplateVariableNode(TemplateVariables.fileLine),
            )),
            TemplateParser.parse("{file_name} - {file_line}")
        )
    }

    @Test
    fun `unknown variable`() {
        val node = TemplateParser.parse("{this_is_not_known}")
        assertEquals(TemplateCompositeNode(listOf()), node)
    }

    @Test
    fun `start escape`() {
        assertAll(
            { assertEquals(TemplateTextNode("{"), TemplateParser.parse("{{")) },
            { assertEquals(TemplateTextNode("{{"), TemplateParser.parse("{{{{")) },
            { assertEquals(TemplateTextNode("This { should be escaped }"), TemplateParser.parse("This {{ should be escaped }")) },
            { assertEquals(TemplateTextNode("This { should { be escaped }"), TemplateParser.parse("This {{ should {{ be escaped }")) },
            {
                assertEquals(
                    TemplateCompositeNode(listOf(
                        TemplateTextNode("This { escaped "),
                        TemplateVariableNode(TemplateVariables.projectName),
                    )),
                    TemplateParser.parse("This {{ escaped {project_name}"),
                )
            },
            {
                assertEquals(
                    TemplateCompositeNode(listOf(
                        TemplateVariableNode(TemplateVariables.projectName),
                        TemplateTextNode(" this { escaped"),
                    )),
                    TemplateParser.parse("{project_name} this {{ escaped"),
                )
            },
        )
    }

    @Test
    fun `end escape`() {
        assertAll(
            { assertEquals(TemplateTextNode("}"), TemplateParser.parse("}}")) },
            { assertEquals(TemplateTextNode("}}"), TemplateParser.parse("}}}}")) },
            { assertEquals(TemplateTextNode("This } should be escaped"), TemplateParser.parse("This }} should be escaped")) },
            {
                assertEquals(
                    TemplateCompositeNode(listOf(
                        TemplateTextNode("This } escaped "),
                        TemplateVariableNode(TemplateVariables.projectName),
                    )),
                    TemplateParser.parse("This }} escaped {project_name}"),
                )
            },
            {
                assertEquals(
                    TemplateCompositeNode(listOf(
                        TemplateVariableNode(TemplateVariables.projectName),
                        TemplateTextNode(" this } escaped"),
                    )),
                    TemplateParser.parse("{project_name} this }} escaped"),
                )
            },
        )
    }

    @Test
    fun `unfinished variable`() {
        assertEquals(TemplateTextNode("this variable { is unfinished"), TemplateParser.parse("this variable { is unfinished"))
    }
}