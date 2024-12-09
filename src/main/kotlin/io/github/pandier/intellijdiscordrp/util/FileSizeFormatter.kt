package io.github.pandier.intellijdiscordrp.util

import kotlin.math.ln
import kotlin.math.pow

/**
 * Converts a file size in bytes to a human-readable string with appropriate unit.
 * Supports units from bytes up to gigabytes.
 *
 * @param bytes The file size in bytes
 * @param decimals Number of decimal places to round to (default: 2)
 * @return A formatted string representing the file size with appropriate unit
 */
fun formatFileSize(bytes: Long, decimals: Int = 2): String {
    if (bytes <= 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB")
    val base = 1024.0

    val unitIndex = (ln(bytes.toDouble()) / ln(base)).toInt()
        .coerceAtMost(units.size - 1)
    val value = bytes / base.pow(unitIndex.toDouble())

    return when {
        unitIndex == 0 -> "$bytes ${units[unitIndex]}"
        else -> "%.${decimals}f ${units[unitIndex]}".format(value)
    }
}