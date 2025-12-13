import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.vanniktech.maven.publish") version "0.34.0"
}

fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .redirectErrorStream(true)
            .start()
        process.inputStream.bufferedReader().readText().trim().toInt()
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

// Load version from version.properties
val versionProps = Properties().apply {
    file("version.properties").inputStream().use { load(it) }
}

val major = versionProps.getProperty("VERSION_MAJOR")?.toIntOrNull() ?: 0
val minor = versionProps.getProperty("VERSION_MINOR")?.toIntOrNull() ?: 0
val patchBase = versionProps.getProperty("VERSION_PATCH")?.toIntOrNull() ?: 0
val commitCount = getGitCommitCount() // Increment patch based on commits
val versionNameGenerated = "$major.$minor.${patchBase}($commitCount)"
val version = "$major.$minor.$patchBase"

android {
    namespace = "dev.skonar"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "LIBRARY_VERSION",
            "\"$versionNameGenerated\""
        )
    }

    publishing {
        multipleVariants {
            includeBuildTypeValues("release", "debug")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.coil.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui) // ui
    implementation(libs.androidx.ui.graphics) // graphics
    implementation(libs.androidx.ui.tooling.preview) // preview
    implementation(libs.androidx.material3) // Material3
    implementation(libs.androidx.foundation) // foundation
    implementation(libs.androidx.foundation.layout) // layout
    implementation(libs.androidx.animation.core)
    implementation(libs.ui.test.junit4)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

mavenPublishing {
    coordinates(
        groupId = "dev.skonar",
        artifactId = "shimmer-compose",
        version = version
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Shimmer Compose Library")
        description.set("Library used to create shimmering effect in Android apps.")
        inceptionYear.set("2025")
        url.set("https://github.com/sergeykonar/compose-shimmer-effect")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("sergey-konar")
                name.set("Serhii Konar")
                email.set("sergey.konar2002@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/sergeykonar/compose-shimmer-effect")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral()

    // Enable GPG signing for all publications
    signAllPublications()
}

tasks.register("updateReadmeVersion") {
    val versionRegex = """implementation\("dev.skonar:shimmer-compose:.*"\)""".toRegex()
    val readmeFile = file("$rootDir/README.md")

    doLast {
        val text = readmeFile.readText()
        val updatedText = versionRegex.replace(text, """implementation("dev.skonar:shimmer-compose:$version")""")
        readmeFile.writeText(updatedText)
        println("Updated README.md to version $version")
    }
}