import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

android {
    namespace = "com.alzpal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alzpal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Load the API key from local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }
        val apiKey = localProperties.getProperty("apiKey") ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        // Custom samples build type
        create("samples") {
            initWith(getByName("debug"))
        }
    }

    // Define the source sets for the samples build type
    sourceSets.getByName("samples") {
        java.setSrcDirs(listOf("src/main/java", "src/main/kotlin", "../../samples/src/main/java"))
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        buildConfig = true
        compose = true // Enable Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4" // Make sure to include the correct Compose version
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Room for database operations
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.room.testing)

    // Firebase Crashlytics for crash reporting
    implementation(libs.firebase.crashlytics.buildtools)

    // AndroidX core libraries
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Glide for image loading
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Gson for JSON parsing
    implementation(libs.gson)

    // EazeGraph for chart views
    implementation(libs.eazegraph)

    // Timber for logging
    implementation(libs.timber)

    // Generative AI client
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // Google Guava and Reactive Streams
    implementation("com.google.guava:guava:33.3.1-android")
    implementation("org.reactivestreams:reactive-streams:1.0.4")

    // Jetpack Compose dependencies
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Coil for image loading in Compose
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Lifecycle components for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.2")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.1")

    // Compose testing dependencies
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.0-alpha02")
}
