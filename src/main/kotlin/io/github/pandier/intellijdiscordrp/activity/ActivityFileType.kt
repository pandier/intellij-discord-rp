package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeNames: Set<String> = hashSetOf(),
    val regex: Regex? = null,
    val extensions: Set<String> = hashSetOf(),
    val replaceFileName: String? = null,
    iconFile: String = "fallback.png",
) {
    /*
     * Files (icons/file)
     */
    ASTRO(
        friendlyName = "Astro",
        typeNames = hashSetOf("astro"),
        extensions = hashSetOf("astro"),
        iconFile = "astro.png",
    ),
    BATCH(
        friendlyName = "Batch",
        typeNames = hashSetOf("batch"),
        extensions = hashSetOf("bat", "cmd"),
        iconFile = "batch.png",
    ),
    C(
        friendlyName = "C",
        typeNames = hashSetOf("c"),
        extensions = hashSetOf("c"),
        iconFile = "c.png"
    ),
    CARGO(
        friendlyName = "Cargo",
        typeNames = hashSetOf("cargo"),
        regex = "^Cargo\\.(toml|lock)\$".toRegex(),
        iconFile = "cargo.png",
    ),
    CPP(
        friendlyName = "C++",
        typeNames = hashSetOf("c++"),
        extensions = hashSetOf("cpp", "h", "hpp", "tcc"),
        iconFile = "cpp.png"
    ),
    CSHARP(
        friendlyName = "C#",
        typeNames = hashSetOf("c#"),
        extensions = hashSetOf("cs"),
        iconFile = "csharp.png"
    ),
    CSS(
        friendlyName = "CSS",
        typeNames = hashSetOf("css"),
        extensions = hashSetOf("css"),
        iconFile = "css.png"
    ),
    DART(
        friendlyName = "Dart",
        typeNames = hashSetOf("dart"),
        extensions = hashSetOf("dart"),
        iconFile = "dart.png"
    ),
    DOCKERFILE(
        friendlyName = "Dockerfile",
        typeNames = hashSetOf("dockerfile"),
        regex = "^(.*\\.)?dockerfile\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "docker.png"
    ),
    DOCKERIGNORE(
        friendlyName = "Dockerignore",
        typeNames = hashSetOf("dockerignore file"),
        extensions = hashSetOf("dockerignore"),
        iconFile = "docker.png",
    ),
    ENV(
        friendlyName = ".env",
        regex = "^\\.env$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "env.png",
    ),
    FSHARP(
        friendlyName = "F#",
        typeNames = hashSetOf("f#"),
        extensions = hashSetOf("fs"),
        iconFile = "fsharp.png"
    ),
    GITDIFF(
        friendlyName = "Gitdiff",
        typeNames = hashSetOf("diff"),
        replaceFileName = "diff",
        iconFile = "git.png",
    ),
    GITIGNORE(
        friendlyName = "Gitignore",
        typeNames = hashSetOf("gitignore file"),
        extensions = hashSetOf("gitignore"),
        iconFile = "git.png",
    ),
    GO(
        friendlyName = "Go",
        typeNames = hashSetOf("go"),
        extensions = hashSetOf("go"),
        iconFile = "go.png"
    ),
    GRADLE(
        friendlyName = "Gradle",
        typeNames = hashSetOf("gradle"),
        extensions = hashSetOf("gradle"),
        iconFile = "gradle.png"
    ),
    GROOVY(
        friendlyName = "Groovy",
        typeNames = hashSetOf("groovy"),
        extensions = hashSetOf("groovy", "gy"),
        iconFile = "groovy.png"
    ),
    HTML(
        friendlyName = "HTML",
        typeNames = hashSetOf("html"),
        extensions = hashSetOf("htm", "html", "xhtm", "xhtml"),
        iconFile = "html.png"
    ),
    HTTP(
        friendlyName = "HTTP requests",
        typeNames = hashSetOf("http requests"),
        extensions = hashSetOf("http", "rest"),
        iconFile = "http.png"
    ),
    IMAGE(
        friendlyName = "Image",
        typeNames = hashSetOf("image"),
        extensions = hashSetOf("png", "gif", "jpg", "jpeg", "svg", "webp", "tif", "tiff", "ico"),
        iconFile = "image.png"
    ),
    JAVA(
        friendlyName = "Java",
        typeNames = hashSetOf("java"),
        extensions = hashSetOf("java"),
        iconFile = "java.png"
    ),
    JAVASCRIPT(
        friendlyName = "JavaScript",
        typeNames = hashSetOf("javascript"),
        extensions = hashSetOf("js", "mjs"),
        iconFile = "javascript.png"
    ),
    JSON(
        friendlyName = "JSON",
        typeNames = hashSetOf("json"),
        extensions = hashSetOf("json", "jsonc"),
        iconFile = "json.png"
    ),
    JSX(
        friendlyName = "JSX",
        typeNames = hashSetOf("jsx"),
        extensions = hashSetOf("jsx"),
        iconFile = "jsx.png"
    ),
    KOTLIN(
        friendlyName = "Kotlin",
        typeNames = hashSetOf("kotlin"),
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
        typeNames = hashSetOf("markdown"),
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
        typeNames = hashSetOf("php"),
        extensions = hashSetOf("php"),
        iconFile = "php.png"
    ),
    PRISMA(
        friendlyName = "Prisma",
        typeNames = hashSetOf("prisma"),
        extensions = hashSetOf("prisma"),
        iconFile = "prisma.png"
    ),
    PROTOBUF(
        friendlyName = "Protocol Buffer",
        typeNames = hashSetOf("protobuf"),
        extensions = hashSetOf("proto"),
        iconFile = "protobuf.png"
    ),
    PYTHON(
        friendlyName = "Python",
        typeNames = hashSetOf("python"),
        extensions = hashSetOf("py"),
        iconFile = "python.png"
    ),
    RUBY(
        friendlyName = "Ruby",
        typeNames = hashSetOf("ruby"),
        extensions = hashSetOf("rb"),
        iconFile = "ruby.png"
    ),
    RUST(
        friendlyName = "Rust",
        typeNames = hashSetOf("rust"),
        extensions = hashSetOf("rs"),
        iconFile = "rust.png"
    ),
    SASS(
        friendlyName = "Sass",
        typeNames = hashSetOf("sass style sheet"),
        extensions = hashSetOf("sass", "scss"),
        iconFile = "sass.png"
    ),
    SCALA(
        friendlyName = "Scala",
        typeNames = hashSetOf("scala files"),
        extensions = hashSetOf("scala", "sc"),
        iconFile = "scala.png"
    ),
    SHELL(
        friendlyName = "Shell script",
        typeNames = hashSetOf("shell script"),
        extensions = hashSetOf("sh", "bash"),
        iconFile = "shell.png"
    ),
    SQL(
        friendlyName = "SQL",
        typeNames = hashSetOf("sql"),
        extensions = hashSetOf("sql"),
        iconFile = "sql.png"
    ),
    SVELTE(
        friendlyName = "Svelte",
        typeNames = hashSetOf("svelte"),
        extensions = hashSetOf("svelte"),
        iconFile = "svelte.png"
    ),
    TOML(
        friendlyName = "TOML",
        typeNames = hashSetOf("toml"),
        extensions = hashSetOf("toml"),
        iconFile = "toml.png"
    ),
    TSX(
        friendlyName = "TSX",
        typeNames = hashSetOf("tsx"),
        extensions = hashSetOf("tsx"),
        iconFile = "tsx.png"
    ),
    TYPESCRIPT(
        friendlyName = "TypeScript",
        typeNames = hashSetOf("typescript"),
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
        typeNames = hashSetOf("visual basic"),
        extensions = hashSetOf("vb"),
        iconFile = "visualbasic.png"
    ),
    VUEJS(
        friendlyName = "Vue.js",
        typeNames = hashSetOf("vuejs"),
        extensions = hashSetOf("vue"),
        iconFile = "vuejs.png"
    ),
    WRITERSIDE_TOPIC(
        friendlyName = "Writerside Topic",
        typeNames = hashSetOf("writerside topic"),
        extensions = hashSetOf("topic"),
        iconFile = "writersidetopic.png"
    ),
    WRITERSIDE_INSTANCE(
        friendlyName = "Writerside Instance",
        typeNames = hashSetOf("writerside instance"),
        extensions = hashSetOf("tree"),
        iconFile = "xml.png"
    ),
    WRITERSIDE_LIST(
        friendlyName = "Writerside List",
        typeNames = hashSetOf("writerside list"),
        extensions = hashSetOf("list"),
        iconFile = "xml.png"
    ),
    XML(
        friendlyName = "XML",
        typeNames = hashSetOf("xml"),
        extensions = hashSetOf("xml"),
        iconFile = "xml.png"
    ),
    YAML(
        friendlyName = "YAML",
        typeNames = hashSetOf("yaml"),
        extensions = hashSetOf("yml", "yaml"),
        iconFile = "yaml.png"
    ),

    /*
     * Tools (icons/file/tool)
     */
    DATABASE_VIEWER(
        friendlyName = "Database Viewer",
        typeNames = hashSetOf("database element"),
        iconFile = "tool/databaseviewer.png",
    ),

    /*
     * Fallback
     */
    OTHER(friendlyName = "File");

    /**
     * Maps for looking up file types by extension or type name.
     */
    object Lookup {
        val byExtension = buildMap { values().forEach { fileType -> fileType.extensions.forEach { extension -> put(extension, fileType) } } }
        val byTypeName = buildMap { values().forEach { fileType -> fileType.typeNames.forEach { typeName -> put(typeName, fileType) } } }
    }

    val icon: String = "$FILE_ICON_REPOSITORY/$iconFile"

    override fun toString(): String =
        friendlyName
}

val VirtualFile.activityFileType: ActivityFileType?
    get() {
        val name = name
        return ActivityFileType.values().find { it.regex?.matches(name) == true }
            ?: ActivityFileType.Lookup.byTypeName[fileType.name.lowercase()]
            ?: ActivityFileType.Lookup.byExtension[extension?.lowercase()]
    }
