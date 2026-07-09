package io.github.pandier.intellijdiscordrp.template

import io.github.pandier.intellijdiscordrp.activity.ActivityContext
import io.github.pandier.intellijdiscordrp.activity.ActivityDisplayMode

/**
 * Represents a variable that can be used in text fields.
 * All variables are defined in [TemplateVariables].
 */
class TemplateVariable(
    val name: String,
    val description: String,
    val displayMode: ActivityDisplayMode,
    private val getter: ActivityContext.() -> String?,
    /**
     * Returns an error message if the variable is not available in the current IDE environment (or null otherwise).
     * This is primarily used in UI to indicate whether a variable is available and doesn't actually affect
     * any functionality. The getter will still be called for the variable even when this check returns false.
     * Defaults to null (available).
     */
    val availabilityCheck: () -> String? = { null },
) {
    /**
     * Returns the full name of the variable with the brackets.
     */
    override fun toString(): String =
        "{$name}"

    /**
     * Returns the value of this variable in the given [ActivityContext].
     * The result can be null if the retrieval of the value was unsuccessful.
     */
    fun getValue(context: ActivityContext): String? =
        getter(context)

    fun supports(displayMode: ActivityDisplayMode): Boolean =
        this.displayMode.ordinal <= displayMode.ordinal
}
