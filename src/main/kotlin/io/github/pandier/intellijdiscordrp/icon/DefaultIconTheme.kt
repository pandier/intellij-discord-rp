package io.github.pandier.intellijdiscordrp.icon

object DefaultIconTheme : IconTheme {
    private const val ICON_REPOSITORY = "https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/icons"
    private const val APPLICATION = "$ICON_REPOSITORY/idea.png"
    private const val FILE_TYPE_FALLBACK = "$ICON_REPOSITORY/file.png"

    private val fileTypeMapping = mapOf(
        "java" to "$ICON_REPOSITORY/java.png",
        "rust" to "$ICON_REPOSITORY/rust.png",
        "kotlin" to "$ICON_REPOSITORY/kotlin.png",
        "javascript" to "$ICON_REPOSITORY/javascript.png",
        "typescript" to "$ICON_REPOSITORY/typescript.png",
    )

    override fun getApplication(productCode: String): String =
        APPLICATION

    override fun getFile(type: String): String =
        fileTypeMapping[type.lowercase()] ?: FILE_TYPE_FALLBACK
}