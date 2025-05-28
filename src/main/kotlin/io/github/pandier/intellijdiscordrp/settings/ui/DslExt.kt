package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MutableProperty
import javax.swing.JComponent
import javax.swing.JTextField
import kotlin.reflect.KMutableProperty0

fun <T : JComponent> Cell<T>.errorOnInput(
    @NlsContexts.DialogMessage message: String,
    condition: (T) -> Boolean
): Cell<T> =
    validationOnInput { if (condition(it)) error(message) else null }

fun <T : JTextField> Cell<T>.maxLength(max: Int): Cell<T> = this
    .errorOnInput("Cannot be longer than $max characters") { it.text.isNotEmpty() && it.text.length > max }
    .errorOnApply("Cannot be longer than $max characters") { it.text.isNotEmpty() && it.text.length > max }

fun <T : JTextField> Cell<T>.required(): Cell<T> =
    errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }

fun <T : JBTextField> Cell<T>.optional(): Cell<T> =
    this.also { it.component.emptyText.text = "Optional" }

fun <T, V> KMutableProperty0<T>.map(mapper: (T) -> KMutableProperty0<V>): MutableProperty<V> =
    MutableProperty({ mapper(get()).get() }, { mapper(get()).set(it) })

fun <T, V> MutableProperty<T>.map(mapper: (T) -> KMutableProperty0<V>): MutableProperty<V> =
    MutableProperty({ mapper(get()).get() }, { mapper(get()).set(it) })
