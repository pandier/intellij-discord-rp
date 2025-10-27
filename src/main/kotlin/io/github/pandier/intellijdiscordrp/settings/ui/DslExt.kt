package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MutableProperty
import io.github.pandier.intellijdiscordrp.DiscordRichPresenceBundle
import io.github.pandier.intellijdiscordrp.util.urlRegex
import javax.swing.JComponent
import javax.swing.JTextField
import kotlin.reflect.KMutableProperty0

fun <T : JComponent> Cell<T>.warnOnApply(
    @NlsContexts.DialogMessage message: String,
    condition: (T) -> Boolean
): Cell<T> =
    validationOnApply { if (condition(it)) warning(message) else null }

fun <T : JComponent> Cell<T>.errorOnInput(
    @NlsContexts.DialogMessage message: String,
    condition: (T) -> Boolean
): Cell<T> =
    validationOnInput { if (condition(it)) error(message) else null }

fun <T : JTextField> Cell<T>.maxLength(max: Int): Cell<T> = this
    .errorOnInput(DiscordRichPresenceBundle.message("dialog.validation.maxLength", max)) { it.text.isNotEmpty() && it.text.length > max }
    .errorOnApply(DiscordRichPresenceBundle.message("dialog.validation.maxLength", max)) { it.text.isNotEmpty() && it.text.length > max }

fun <T : JTextField> Cell<T>.required(): Cell<T> =
    errorOnApply(DiscordRichPresenceBundle.message("dialog.validation.required")) { it.isEnabled && it.text.isEmpty() }

fun <T : JBTextField> Cell<T>.optional(): Cell<T> =
    also { it.component.emptyText.text = "Optional" }

fun <T : JTextField> Cell<T>.validateUrlWithVariables(): Cell<T> =
    warnOnApply(DiscordRichPresenceBundle.message("dialog.validation.invalidUrl")) { it.text.isNotEmpty() && (!it.text.contains('{') || !it.text.contains('}')) && !urlRegex.matches(it.text) }

fun <T, V> KMutableProperty0<T>.map(mapper: (T) -> KMutableProperty0<V>): MutableProperty<V> =
    MutableProperty({ mapper(get()).get() }, { mapper(get()).set(it) })

fun <T, V> MutableProperty<T>.map(mapper: (T) -> KMutableProperty0<V>): MutableProperty<V> =
    MutableProperty({ mapper(get()).get() }, { mapper(get()).set(it) })
