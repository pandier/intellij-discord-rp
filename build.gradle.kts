import org.jetbrains.changelog.Changelog

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.0.1"
    id("org.jetbrains.changelog") version "2.2.0"
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2023.1.6")

        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }

    implementation("io.github.vyfor:kpresence:0.6.3") {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
    }
}

intellijPlatform {
    pluginConfiguration {
        changeNotes.set(provider {
            changelog.renderItem(
                (changelog.getOrNull(project.version.toString()) ?: changelog.getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML
            )
        })

        ideaVersion {
            untilBuild.set(provider { null })
        }
    }

    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

kotlin {
    jvmToolchain(17)
}

changelog {
    groups.empty()
    repositoryUrl.set(providers.gradleProperty("repository"))
}
