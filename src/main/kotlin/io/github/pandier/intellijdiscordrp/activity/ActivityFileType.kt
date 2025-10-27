package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.activity.icon.FILE_ICON_REPOSITORY

enum class ActivityFileType(
    val friendlyName: String,
    val typeNames: List<String> = listOf(),
    val regex: Regex? = null,
    val extensions: List<String> = listOf(),
    val replaceFileName: String? = null,
    iconFile: String = "fallback.png",
) {
    /*
     * Files (icons/file)
     */
    ASTRO(
        friendlyName = "Astro",
        typeNames = listOf("astro"),
        extensions = listOf("astro"),
        iconFile = "astro.png",
    ),
    BATCH(
        friendlyName = "Batch",
        typeNames = listOf("batch"),
        extensions = listOf("bat", "cmd"),
        iconFile = "batch.png",
    ),
    C(
        friendlyName = "C",
        typeNames = listOf("c"),
        extensions = listOf("c"),
        iconFile = "c.png"
    ),
    CARGO(
        friendlyName = "Cargo",
        typeNames = listOf("cargo"),
        regex = "^Cargo\\.(toml|lock)\$".toRegex(),
        iconFile = "cargo.png",
    ),
    CPP(
        friendlyName = "C++",
        typeNames = listOf("c++"),
        extensions = listOf("cpp", "h", "hpp", "tcc"),
        iconFile = "cpp.png"
    ),
    CSHARP(
        friendlyName = "C#",
        typeNames = listOf("c#"),
        extensions = listOf("cs"),
        iconFile = "csharp.png"
    ),
    CSS(
        friendlyName = "CSS",
        typeNames = listOf("css"),
        extensions = listOf("css"),
        iconFile = "css.png"
    ),
    DART(
        friendlyName = "Dart",
        typeNames = listOf("dart"),
        extensions = listOf("dart"),
        iconFile = "dart.png"
    ),
    DOCKERFILE(
        friendlyName = "Dockerfile",
        typeNames = listOf("dockerfile"),
        regex = "^(.*\\.)?dockerfile\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "docker.png"
    ),
    DOCKERIGNORE(
        friendlyName = "Dockerignore",
        typeNames = listOf("dockerignore file"),
        extensions = listOf("dockerignore"),
        iconFile = "docker.png",
    ),
    ENV(
        friendlyName = ".env",
        regex = "^\\.env$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "env.png",
    ),
    FSHARP(
        friendlyName = "F#",
        typeNames = listOf("f#"),
        extensions = listOf("fs"),
        iconFile = "fsharp.png"
    ),
    GITDIFF(
        friendlyName = "Gitdiff",
        typeNames = listOf("diff"),
        replaceFileName = "diff",
        iconFile = "git.png",
    ),
    GITIGNORE(
        friendlyName = "Gitignore",
        typeNames = listOf("gitignore file"),
        extensions = listOf("gitignore"),
        iconFile = "git.png",
    ),
    GO(
        friendlyName = "Go",
        typeNames = listOf("go"),
        extensions = listOf("go"),
        iconFile = "go.png"
    ),
    GRADLE(
        friendlyName = "Gradle",
        typeNames = listOf("gradle"),
        extensions = listOf("gradle"),
        iconFile = "gradle.png"
    ),
    GROOVY(
        friendlyName = "Groovy",
        typeNames = listOf("groovy"),
        extensions = listOf("groovy", "gy"),
        iconFile = "groovy.png"
    ),
    HTML(
        friendlyName = "HTML",
        typeNames = listOf("html"),
        extensions = listOf("htm", "html", "xhtm", "xhtml"),
        iconFile = "html.png"
    ),
    HTTP(
        friendlyName = "HTTP requests",
        typeNames = listOf("http requests"),
        extensions = listOf("http", "rest"),
        iconFile = "http.png"
    ),
    IMAGE(
        friendlyName = "Image",
        typeNames = listOf("image"),
        extensions = listOf("png", "gif", "jpg", "jpeg", "svg", "webp", "tif", "tiff", "ico"),
        iconFile = "image.png"
    ),
    JAVA(
        friendlyName = "Java",
        typeNames = listOf("java"),
        extensions = listOf("java"),
        iconFile = "java.png"
    ),
    JAVASCRIPT(
        friendlyName = "JavaScript",
        typeNames = listOf("javascript"),
        extensions = listOf("js", "mjs"),
        iconFile = "javascript.png"
    ),
    JSON(
        friendlyName = "JSON",
        typeNames = listOf("json"),
        extensions = listOf("json", "jsonc"),
        iconFile = "json.png"
    ),
    JSX(
        friendlyName = "JSX",
        typeNames = listOf("jsx"),
        extensions = listOf("jsx"),
        iconFile = "jsx.png"
    ),
    KOTLIN(
        friendlyName = "Kotlin",
        typeNames = listOf("kotlin"),
        extensions = listOf("kt", "kts"),
        iconFile = "kotlin.png"
    ),
    LICENSE(
        friendlyName = "License",
        regex = "^license(\\.(md|txt))?$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "license.png"
    ),
    MARKDOWN(
        friendlyName = "Markdown",
        typeNames = listOf("markdown"),
        extensions = listOf("md", "mdx"),
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
        typeNames = listOf("php"),
        extensions = listOf("php"),
        iconFile = "php.png"
    ),
    PRISMA(
        friendlyName = "Prisma",
        typeNames = listOf("prisma"),
        extensions = listOf("prisma"),
        iconFile = "prisma.png"
    ),
    PROTOBUF(
        friendlyName = "Protocol Buffer",
        typeNames = listOf("protobuf"),
        extensions = listOf("proto"),
        iconFile = "protobuf.png"
    ),
    PYTHON(
        friendlyName = "Python",
        typeNames = listOf("python"),
        extensions = listOf("py"),
        iconFile = "python.png"
    ),
    RUBY(
        friendlyName = "Ruby",
        typeNames = listOf("ruby"),
        extensions = listOf("rb"),
        iconFile = "ruby.png"
    ),
    RUST(
        friendlyName = "Rust",
        typeNames = listOf("rust"),
        extensions = listOf("rs"),
        iconFile = "rust.png"
    ),
    SASS(
        friendlyName = "Sass",
        typeNames = listOf("sass style sheet"),
        extensions = listOf("sass", "scss"),
        iconFile = "sass.png"
    ),
    SCALA(
        friendlyName = "Scala",
        typeNames = listOf("scala files"),
        extensions = listOf("scala", "sc"),
        iconFile = "scala.png"
    ),
    SHADER(
        friendlyName = "Shader",
        typeNames = listOf("glsl", "usf", "ush", "shaderlab"),
        extensions = listOf("glsl", "usf", "ush", "shader", "slsl", "vert", "frag", "geom", "comp", "tesc", "tese", "hlsl", "fx"),
        iconFile = "shader.png"
    ),
    SHELL(
        friendlyName = "Shell script",
        typeNames = listOf("shell script"),
        extensions = listOf("sh", "bash"),
        iconFile = "shell.png"
    ),
    SQL(
        friendlyName = "SQL",
        typeNames = listOf("sql"),
        extensions = listOf("sql"),
        iconFile = "sql.png"
    ),
    SVELTE(
        friendlyName = "Svelte",
        typeNames = listOf("svelte"),
        extensions = listOf("svelte"),
        iconFile = "svelte.png"
    ),
    TOML(
        friendlyName = "TOML",
        typeNames = listOf("toml"),
        extensions = listOf("toml"),
        iconFile = "toml.png"
    ),
    TSX(
        friendlyName = "TSX",
        typeNames = listOf("tsx"),
        extensions = listOf("tsx"),
        iconFile = "tsx.png"
    ),
    TYPESCRIPT(
        friendlyName = "TypeScript",
        typeNames = listOf("typescript"),
        extensions = listOf("ts"),
        iconFile = "typescript.png"
    ),
    TYPESCRIPT_CONFIG(
        friendlyName = "TypeScript config",
        regex = "^tsconfig.json\$".toRegex(RegexOption.IGNORE_CASE),
        iconFile = "tsconfig.png"
    ),
    VISUAL_BASIC(
        friendlyName = "Visual Basic",
        typeNames = listOf("visual basic"),
        extensions = listOf("vb"),
        iconFile = "visualbasic.png"
    ),
    VUEJS(
        friendlyName = "Vue.js",
        typeNames = listOf("vuejs"),
        extensions = listOf("vue"),
        iconFile = "vuejs.png"
    ),
    WRITERSIDE_TOPIC(
        friendlyName = "Writerside Topic",
        typeNames = listOf("writerside topic"),
        extensions = listOf("topic"),
        iconFile = "writersidetopic.png"
    ),
    WRITERSIDE_INSTANCE(
        friendlyName = "Writerside Instance",
        typeNames = listOf("writerside instance"),
        extensions = listOf("tree"),
        iconFile = "xml.png"
    ),
    WRITERSIDE_LIST(
        friendlyName = "Writerside List",
        typeNames = listOf("writerside list"),
        extensions = listOf("list"),
        iconFile = "xml.png"
    ),
    XML(
        friendlyName = "XML",
        typeNames = listOf("xml"),
        extensions = listOf("xml"),
        iconFile = "xml.png"
    ),
    YAML(
        friendlyName = "YAML",
        typeNames = listOf("yaml"),
        extensions = listOf("yml", "yaml"),
        iconFile = "yaml.png"
    ),

    /*
     * Tools (icons/file/tool)
     */
    DATABASE_VIEWER(
        friendlyName = "Database Viewer",
        typeNames = listOf("database element"),
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
