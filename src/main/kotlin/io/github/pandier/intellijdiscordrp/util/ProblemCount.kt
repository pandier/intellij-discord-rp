package io.github.pandier.intellijdiscordrp.util

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ProblemCount(
    val total: Int = 0,
    val errors: Int = 0,
    val warnings: Int = 0,
) {
    companion object {
        suspend fun get(editor: Editor, project: Project? = editor.project): ProblemCount =
            get(editor.document, project)

        suspend fun get(document: Document, project: Project?): ProblemCount {
            return withContext(Dispatchers.EDT) {
                ReadAction.compute<ProblemCount, Exception> {
                    getInternal(document, project)
                }
            }
        }

        private fun getInternal(document: Document, project: Project?): ProblemCount {
            val markupModel = DocumentMarkupModel.forDocument(document, project, false)
                ?: return ProblemCount()
            var errors = 0
            var warnings = 0
            for (highlighter in markupModel.allHighlighters) {
                val info = HighlightInfo.fromRangeHighlighter(highlighter) ?: continue
                when (info.severity) {
                    HighlightSeverity.ERROR -> errors++
                    HighlightSeverity.WARNING -> warnings++
                }
            }
            return ProblemCount(errors + warnings, errors, warnings)
        }
    }
}