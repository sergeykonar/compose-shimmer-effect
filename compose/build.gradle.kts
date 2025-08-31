import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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

android {
    namespace = "konar.ui.view"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        val nasaApiKey = System.getenv("NASA_API_KEY")
            ?: findProperty("NASA_API_KEY")?.toString()
            ?: throw GradleException("NASA_API_KEY is missing. Set it in gradle.properties or as an env variable")

        buildConfigField("String", "NASA_API_KEY", "\"$nasaApiKey\"")
        buildConfigField(
            "String",
            "LIBRARY_VERSION",
            versionNameGenerated
        )
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
