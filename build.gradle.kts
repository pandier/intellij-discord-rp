import org.jetbrains.changelog.Changelog

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.intellij") version "1.17.2"
    id("org.jetbrains.changelog") version "2.2.0"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.JnCrMx:discord-game-sdk4j:java-impl-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

intellij {
    version.set("2022.3.3")
}

changelog {
    groups.empty()
    repositoryUrl.set(providers.gradleProperty("repository"))
}

tasks {
    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("241.*")

        changeNotes.set(provider {
            changelog.renderItem(
                (changelog.getOrNull(project.version.toString()) ?: changelog.getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML
            )
        })
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
