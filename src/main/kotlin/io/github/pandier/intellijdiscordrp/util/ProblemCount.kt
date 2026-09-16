package io.github.pandier.intellijdiscordrp.util

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.readAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.project.Project

data class ProblemCount(
    val total: Int = 0,
    val errors: Int = 0,
    val warnings: Int = 0,
) {
    companion object {
        suspend fun get(editor: Editor, project: Project? = editor.project): ProblemCount {
            return get(editor.document, project)
        }

        suspend fun get(document: Document, project: Project?): ProblemCount {
            return readAction { getInternal(document, project) }
        }

        private fun getInternal(document: Document, project: Project?): ProblemCount {
            val markupModel = DocumentMarkupModel.forDocument(document, project, false) as? MarkupModelEx
                ?: return ProblemCount()
            var errors = 0
            var warnings = 0
            // Unlike getAllHighlighters, this traversal doesn't require the event dispatch thread
            markupModel.processRangeHighlightersOverlappingWith(0, document.textLength) { highlighter ->
                val info = HighlightInfo.fromRangeHighlighter(highlighter)
                if (info != null) {
                    when (info.severity) {
                        HighlightSeverity.ERROR -> errors++
                        HighlightSeverity.WARNING -> warnings++
                    }
                }
                true
            }
            return ProblemCount(errors + warnings, errors, warnings)
        }
    }
}