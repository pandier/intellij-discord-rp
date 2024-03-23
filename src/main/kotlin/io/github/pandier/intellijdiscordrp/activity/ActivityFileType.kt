package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.fileTypes.FileType
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val typeName: String? = null,
    private val iconFile: String = "fallback.png",
) {
    JAVA("java", "java.png"),
    KOTLIN("kotlin", "kotlin.png"),
    RUST("rust", "rust.png"),
    JAVASCRIPT("javascript", "javascript.png"),
    TYPESCRIPT("typescript", "typescript.png"),
    OTHER;

    val icon: String
        get() = "$FILE_ICON_REPOSITORY/$iconFile"
}

val FileType.activityFileType: ActivityFileType
    get() {
        val typeNameLowercase = name.lowercase()
        return ActivityFileType.values().find { it.typeName == typeNameLowercase }
            ?: ActivityFileType.OTHER
    }
