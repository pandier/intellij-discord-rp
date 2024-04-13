package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeName: String? = null,
    val extensions: Set<String> = hashSetOf(),
    private val iconFile: String = "fallback.png",
) {
    C("C", "c", hashSetOf("c"), "c.png"),
    CPP("C++", "c++", hashSetOf("cpp", "h", "hpp", "tcc"), "cpp.png"),
    CSHARP("C#", "c#", hashSetOf("cs"), "csharp.png"),
    CSS("CSS", "css", hashSetOf("css"), "css.png"),
    DART("Dart", "dart", hashSetOf("dart"), "dart.png"),
    GO("Go", "go", hashSetOf("go"), "go.png"),
    GRADLE("Gradle", "gradle", hashSetOf("gradle"), "gradle.png"),
    GROOVY("Groovy", "groovy", hashSetOf("groovy", "gy"), "groovy.png"),
    HTML("HTML", "html", hashSetOf("htm", "html", "xhtm", "xhtml"), "html.png"),
    JAVA("Java", "java", hashSetOf("java"), "java.png"),
    JAVASCRIPT("JavaScript", "javascript", hashSetOf("js", "jsx"), "javascript.png"),
    JSON("JSON", "json", hashSetOf("json", "jsonc"), "json.png"),
    KOTLIN("Kotlin", "kotlin", hashSetOf("kt", "kts"), "kotlin.png"),
    MARKDOWN("Markdown", "markdown", hashSetOf("md", "mdx"), "markdown.png"),
    PHP("PHP", "php", hashSetOf("php"), "php.png"),
    PYTHON("Python", "python", hashSetOf("py"), "python.png"),
    RUST("Rust", "rust", hashSetOf("rs"), "rust.png"),
    TOML("TOML", "toml", hashSetOf("toml"), "toml.png"),
    TYPESCRIPT("TypeScript", "typescript", hashSetOf("ts"), "typescript.png"),
    XML("XML", "xml", hashSetOf("xml"), "xml.png"),
    YAML("YAML", "yaml", hashSetOf("yml", "yaml"), "yaml.png"),
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
