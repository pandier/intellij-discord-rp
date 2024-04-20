package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.Cell
import javax.swing.JComponent

fun <T : JComponent> Cell<T>.errorOnInput(
    @NlsContexts.DialogMessage message: String,
    condition: (T) -> Boolean
): Cell<T> =
    validationOnInput { if (condition(it)) error(message) else null }
