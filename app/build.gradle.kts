import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

// ── Load credentials from root .env ──────────────────────────
fun loadEnv(): Map<String, String> {
    val envFile = rootProject.file(".env")
    if (!envFile.exists()) return emptyMap()
    return envFile.readLines()
        .filter { line -> line.isNotBlank() && !line.startsWith("#") && line.contains("=") }
        .associate { line ->
            val idx = line.indexOf('=')
            line.substring(0, idx).trim() to line.substring(idx + 1).trim()
        }
}
val env = loadEnv()

android {
    namespace = "com.example.travelaks"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.travelaks"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject .env values into BuildConfig — accessible as BuildConfig.CHATBOT_BASE_URL etc.
        buildConfigField("String", "CHATBOT_BASE_URL", "\"${env["CHATBOT_BASE_URL"] ?: ""}\"")
        buildConfigField("String", "CHATBOT_API_KEY",  "\"${env["CHATBOT_API_KEY"]  ?: ""}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.noties.markwon:core:4.6.2")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
}
