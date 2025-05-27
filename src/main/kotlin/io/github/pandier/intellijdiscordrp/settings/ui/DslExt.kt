package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MutableProperty
import javax.swing.JComponent
import kotlin.reflect.KMutableProperty0

fun <T : JComponent> Cell<T>.errorOnInput(
    @NlsContexts.DialogMessage message: String,
    condition: (T) -> Boolean
): Cell<T> =
    validationOnInput { if (condition(it)) error(message) else null }

fun <T, V> KMutableProperty0<T>.map(mapper: (T) -> KMutableProperty0<V>): MutableProperty<V> =
    MutableProperty({ mapper(get()).get() }, { mapper(get()).set(it) })
