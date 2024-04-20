package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeName: String? = null,
    val regex: Regex? = null,
    val extensions: Set<String> = hashSetOf(),
    iconFile: String = "fallback.png",
) {
    C(
        friendlyName = "C",
        typeName = "c",
        extensions = hashSetOf("c"),
        iconFile = "c.png"
    ),
    CPP(
        friendlyName = "C++",
        typeName = "c++",
        extensions = hashSetOf("cpp", "h", "hpp", "tcc"),
        iconFile = "cpp.png"
    ),
    CSHARP(
        friendlyName = "C#",
        typeName = "c#",
        extensions = hashSetOf("cs"),
        iconFile = "csharp.png"
    ),
    CSS(
        friendlyName = "CSS",
        typeName = "css",
        extensions = hashSetOf("css"),
        iconFile = "css.png"
    ),
    DART(
        friendlyName = "Dart",
        typeName = "dart",
        extensions = hashSetOf("dart"),
        iconFile = "dart.png"
    ),
    GO(
        friendlyName = "Go",
        typeName = "go",
        extensions = hashSetOf("go"),
        iconFile = "go.png"
    ),
    GRADLE(
        friendlyName = "Gradle",
        typeName = "gradle",
        extensions = hashSetOf("gradle"),
        iconFile = "gradle.png"
    ),
    GROOVY(
        friendlyName = "Groovy",
        typeName = "groovy",
        extensions = hashSetOf("groovy", "gy"),
        iconFile = "groovy.png"
    ),
    HTML(
        friendlyName = "HTML",
        typeName = "html",
        extensions = hashSetOf("htm", "html", "xhtm", "xhtml"),
        iconFile = "html.png"
    ),
    IMAGE(
        friendlyName = "Image",
        typeName = "image",
        extensions = hashSetOf("png", "gif", "jpg", "jpeg", "svg", "webp", "tif", "tiff"),
        iconFile = "image.png"
    ),
    JAVA(
        friendlyName = "Java",
        typeName = "java",
        extensions = hashSetOf("java"),
        iconFile = "java.png"
    ),
    JAVASCRIPT(
        friendlyName = "JavaScript",
        typeName = "javascript",
        extensions = hashSetOf("js", "jsx"),
        iconFile = "javascript.png"
    ),
    JSON(
        friendlyName = "JSON",
        typeName = "json",
        extensions = hashSetOf("json", "jsonc"),
        iconFile = "json.png"
    ),
    KOTLIN(
        friendlyName = "Kotlin",
        typeName = "kotlin",
        extensions = hashSetOf("kt", "kts"),
        iconFile = "kotlin.png"
    ),
    MARKDOWN(
        friendlyName = "Markdown",
        typeName = "markdown",
        extensions = hashSetOf("md", "mdx"),
        iconFile = "markdown.png"
    ),
    PHP(
        friendlyName = "PHP",
        typeName = "php",
        extensions = hashSetOf("php"),
        iconFile = "php.png"
    ),
    PYTHON(
        friendlyName = "Python",
        typeName = "python",
        extensions = hashSetOf("py"),
        iconFile = "python.png"
    ),
    RUST(
        friendlyName = "Rust",
        typeName = "rust",
        extensions = hashSetOf("rs"),
        iconFile = "rust.png"
    ),
    TOML(
        friendlyName = "TOML",
        typeName = "toml",
        extensions = hashSetOf("toml"),
        iconFile = "toml.png"
    ),
    TYPESCRIPT(
        friendlyName = "TypeScript",
        typeName = "typescript",
        extensions = hashSetOf("ts"),
        iconFile = "typescript.png"
    ),
    XML(
        friendlyName = "XML",
        typeName = "xml",
        extensions = hashSetOf("xml"),
        iconFile = "xml.png"
    ),
    YAML(
        friendlyName = "YAML",
        typeName = "yaml",
        extensions = hashSetOf("yml", "yaml"),
        iconFile = "yaml.png"
    ),
    OTHER(friendlyName = "File");

    val icon: String = "$FILE_ICON_REPOSITORY/$iconFile"

    override fun toString(): String =
        friendlyName
}

val VirtualFile.activityFileType: ActivityFileType
    get() {
        val name = name
        val typeName = fileType.name.lowercase()
        val extension = extension?.lowercase()
        return ActivityFileType.values()
            .find { it.typeName == typeName || it.extensions.contains(extension) || it.regex?.matches(name) == true }
                ?: ActivityFileType.OTHER
    }
