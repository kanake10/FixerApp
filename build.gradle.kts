// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("com.diffplug.spotless") version "6.24.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

subprojects {
    if (project.name != "build-logic") {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "com.diffplug.spotless")
        apply(plugin = "io.gitlab.arturbosch.detekt")

        spotless {
            kotlin {
                target("**/*.kt")
                targetExclude("**/build/**/*.kt")
                ktlint()
                licenseHeaderFile(
                    rootProject.file("config/copyright.kt"),
                    "^(package|object|import|interface)"
                )
            }
        }

        ktlint {
            android.set(true)
            verbose.set(true)
            outputToConsole.set(true)
        }

        detekt {
            toolVersion = "1.23.6"
            config.from(rootProject.file("config/detekt.yml"))
            buildUponDefaultConfig = true
            allRules = false
            autoCorrect = true
        }
    }
}
