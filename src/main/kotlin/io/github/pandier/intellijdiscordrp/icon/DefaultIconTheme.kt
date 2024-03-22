package io.github.pandier.intellijdiscordrp.icon

object DefaultIconTheme : IconTheme {
    private const val ICON_REPOSITORY = "https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/icons"
    private const val FILE_ICON_REPOSITORY = "$ICON_REPOSITORY/file"
    private const val APPLICATION_ICON_REPOSITORY = "$ICON_REPOSITORY/application"

    private const val APPLICATION_FALLBACK = "$APPLICATION_ICON_REPOSITORY/fallback.png"
    private val applicationMapping = mapOf(
        "ic" to "$APPLICATION_ICON_REPOSITORY/ij.png",
        "iu" to "$APPLICATION_ICON_REPOSITORY/ij.png",
        "ie" to "$APPLICATION_ICON_REPOSITORY/ij.png",
        "pc" to "$APPLICATION_ICON_REPOSITORY/pc.png",
        "py" to "$APPLICATION_ICON_REPOSITORY/pc.png",
        "pe" to "$APPLICATION_ICON_REPOSITORY/pc.png",
        "ps" to "$APPLICATION_ICON_REPOSITORY/ps.png",
        "ws" to "$APPLICATION_ICON_REPOSITORY/ws.png",
        "cl" to "$APPLICATION_ICON_REPOSITORY/cl.png",
        "go" to "$APPLICATION_ICON_REPOSITORY/go.png",
        "rd" to "$APPLICATION_ICON_REPOSITORY/rd.png",
    )

    private const val FILE_FALLBACK = "$FILE_ICON_REPOSITORY/fallback.png"
    private val fileMapping = mapOf(
        "java" to "$FILE_ICON_REPOSITORY/java.png",
        "rust" to "$FILE_ICON_REPOSITORY/rust.png",
        "kotlin" to "$FILE_ICON_REPOSITORY/kotlin.png",
        "javascript" to "$FILE_ICON_REPOSITORY/javascript.png",
        "typescript" to "$FILE_ICON_REPOSITORY/typescript.png",
    )

    override fun getApplication(productCode: String): String =
        applicationMapping[productCode.lowercase()] ?: APPLICATION_FALLBACK

    override fun getFile(type: String): String =
        fileMapping[type.lowercase()] ?: FILE_FALLBACK
}