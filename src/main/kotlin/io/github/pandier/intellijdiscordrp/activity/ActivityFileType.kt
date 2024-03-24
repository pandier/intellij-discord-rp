package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeName: String? = null,
    val extensions: Set<String> = hashSetOf(),
    private val iconFile: String = "fallback.png",
) {
    JAVA("Java", "java", hashSetOf("java"), "java.png"),
    KOTLIN("Kotlin", "kotlin", hashSetOf("kt", "kts"), "kotlin.png"),
    RUST("Rust", "rust", hashSetOf("rs"), "rust.png"),
    JAVASCRIPT("JavaScript", "javascript", hashSetOf("js"), "javascript.png"),
    TYPESCRIPT("TypeScript", "typescript", hashSetOf("ts"), "typescript.png"),
    OTHER("File");

    val icon: String
        get() = "$FILE_ICON_REPOSITORY/$iconFile"

    override fun toString(): String =
        friendlyName
}

val VirtualFile.activityFileType: ActivityFileType
    get() {
        val typeName = fileType.name.lowercase()
        val extension = extension?.lowercase()
        return ActivityFileType.values().find { it.typeName == typeName || it.extensions.contains(extension) }
                ?: ActivityFileType.OTHER
    }
