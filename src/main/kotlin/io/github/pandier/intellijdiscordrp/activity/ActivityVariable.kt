package io.github.pandier.intellijdiscordrp.activity

/**
 * Represents a variable that can be used in text fields.
 * All variables are defined in [ActivityDisplayMode].
 *
 * @see ActivityDisplayMode
 */
class ActivityVariable(
    val name: String,
    val description: String,
    private val getter: ActivityContext.() -> String?
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

    /**
     * Replaces all occurrences of this variable in the given [string]
     * with the value of this variable in the given [ActivityContext].
     *
     * An empty string is used if the retrieval of the value was unsuccessful.
     */
    fun format(string: String, context: ActivityContext): String =
        string.replace(toString(), getValue(context) ?: "")
}