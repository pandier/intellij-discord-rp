package io.github.pandier.intellijdiscordrp.icon

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileTypes.FileType

object DefaultIconTheme : IconTheme {
    private const val PLATFORM = "idea"
    private const val FILE_TYPE_FALLBACK = "file"

    private val fileTypeMapping = mapOf(
        "java" to "java",
        "rust" to "rust",
        "kotlin" to "kotlin",
    )

    override fun getPlatform(info: ApplicationInfo): String =
        PLATFORM

    override fun getFile(type: FileType): String =
        fileTypeMapping[type.name.lowercase()] ?: FILE_TYPE_FALLBACK
}