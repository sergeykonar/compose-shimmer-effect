// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint) apply true
    id("org.jmailen.kotlinter") version "5.2.0" apply true
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply true
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        ktlintRuleset("io.nlopez.compose.rules:ktlint:0.4.18")
    }

    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        enableExperimentalRules.set(true)
    }

    detekt {
        buildUponDefaultConfig = true // preconfigure defaults
        // Use `source` instead of `input`
        source.from("src/main/kotlin")
        config.setFrom(files(rootProject.file("detekt.yml")))
    }

    ktlint {
        filter {
            exclude("**/*.gradle.kts") // ignore Gradle scripts
            exclude("**/build/**") // build outputs
            exclude("**/androidTest/**") // Android instrumented tests
            exclude("**/test/**")
            include("**/src/**/*.kt") // only check Kotlin source files
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "11"
    }
}
