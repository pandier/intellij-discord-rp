package io.github.pandier.intellijdiscordrp.icon

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileTypes.FileType

object DefaultIconTheme : IconTheme {
    private const val PLATFORM = "idea"
    private const val ICON_REPOSITORY = "https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/icons"
    private const val FILE_TYPE_FALLBACK = "$ICON_REPOSITORY/file.png"

    private val fileTypeMapping = mapOf(
        "java" to "$ICON_REPOSITORY/java.png",
        "rust" to "$ICON_REPOSITORY/rust.png",
        "kotlin" to "$ICON_REPOSITORY/kotlin.png",
        "javascript" to "$ICON_REPOSITORY/javascript.png",
        "typescript" to "$ICON_REPOSITORY/typescript.png",
    )

    override fun getPlatform(info: ApplicationInfo): String =
        PLATFORM

    override fun getFile(type: FileType): String =
        fileTypeMapping[type.name.lowercase()] ?: FILE_TYPE_FALLBACK
}