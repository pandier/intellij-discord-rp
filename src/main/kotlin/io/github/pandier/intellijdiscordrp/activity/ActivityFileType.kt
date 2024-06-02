package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeName: String? = null,
    val regex: Regex? = null,
    val extensions: Set<String> = hashSetOf(),
    val replaceFileName: String? = null,
    iconFile: String = "fallback.png",
) {
    BATCH(
        friendlyName = "Batch",
        typeName = "batch",
        extensions = hashSetOf("bat", "cmd"),
        iconFile = "batch.png",
    ),
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
    DOCKERFILE(
        friendlyName = "Dockerfile",
        typeName = "dockerfile",
        regex = "^(.*\\.)?dockerfile\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "docker.png"
    ),
    DOCKERIGNORE(
        friendlyName = "Dockerignore",
        typeName = "dockerignore file",
        extensions = hashSetOf("dockerignore"),
        iconFile = "docker.png",
    ),
    GITDIFF(
        friendlyName = "Gitdiff",
        typeName = "diff",
        replaceFileName = "diff",
        iconFile = "git.png",
    ),
    GITIGNORE(
        friendlyName = "Gitignore",
        typeName = "gitignore file",
        extensions = hashSetOf("gitignore"),
        iconFile = "git.png",
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
    HTTP(
        friendlyName = "HTTP requests",
        typeName = "http requests",
        extensions = hashSetOf("http", "rest"),
        iconFile = "http.png"
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
        extensions = hashSetOf("js", "jsx", "mjs"),
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
    LICENSE(
        friendlyName = "License",
        regex = "^license(\\.(md|txt))?$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "license.png"
    ),
    MARKDOWN(
        friendlyName = "Markdown",
        typeName = "markdown",
        extensions = hashSetOf("md", "mdx"),
        iconFile = "markdown.png"
    ),
    NPM_PACKAGE(
        friendlyName = "NPM package",
        regex = "^package(-lock)?\\.json$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "npm.png"
    ),
    NUXT_CONFIG(
        friendlyName = "Nuxt config",
        regex = "^nuxt.config.ts\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "nuxtjs.png"
    ),
    PHP(
        friendlyName = "PHP",
        typeName = "php",
        extensions = hashSetOf("php"),
        iconFile = "php.png"
    ),
    PRISMA(
        friendlyName = "Prisma",
        typeName = "prisma",
        extensions = hashSetOf("prisma"),
        iconFile = "prisma.png"
    ),
    PYTHON(
        friendlyName = "Python",
        typeName = "python",
        extensions = hashSetOf("py"),
        iconFile = "python.png"
    ),
    RUBY(
        friendlyName = "Ruby",
        typeName = "ruby",
        extensions = hashSetOf("rb"),
        iconFile = "ruby.png"
    ),
    RUST(
        friendlyName = "Rust",
        typeName = "rust",
        extensions = hashSetOf("rs"),
        iconFile = "rust.png"
    ),
    SHELL(
        friendlyName = "Shell script",
        typeName = "shell script",
        extensions = hashSetOf("sh", "bash"),
        iconFile = "shell.png"
    ),
    SQL(
        friendlyName = "SQL",
        typeName = "sql",
        extensions = hashSetOf("sql"),
        iconFile = "sql.png"
    ),
    SVELTE(
        friendlyName = "Svelte",
        typeName = "svelte",
        extensions = hashSetOf("svelte"),
        iconFile = "svelte.png"
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
    TYPESCRIPT_CONFIG(
        friendlyName = "TypeScript config",
        regex = "^tsconfig.json\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "tsconfig.png"
    ),
    VISUAL_BASIC(
        friendlyName = "Visual Basic",
        typeName = "visual basic",
        extensions = hashSetOf("vb"),
        iconFile = "visualbasic.png"
    ),
    VUEJS(
        friendlyName = "Vue.js",
        typeName = "vuejs",
        extensions = hashSetOf("vue"),
        iconFile = "vuejs.png"
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

val VirtualFile.activityFileType: ActivityFileType?
    get() {
        val name = name
        val typeName = fileType.name.lowercase()
        val extension = extension?.lowercase()
        return ActivityFileType.values().find { it.regex?.matches(name) == true }
            ?: ActivityFileType.values().find { it.typeName == typeName || it.extensions.contains(extension) }
    }
