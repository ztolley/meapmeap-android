plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val javaVersion = 21
val javaVersionEnum = when (javaVersion) {
    8 -> JavaVersion.VERSION_1_8
    11 -> JavaVersion.VERSION_11
    17 -> JavaVersion.VERSION_17
    21 -> JavaVersion.VERSION_21
    else -> throw GradleException("Unsupported Java version: $javaVersion")
}

val kotlinJvmTargetEnum = when (javaVersion) {
    8 -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
    11 -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    17 -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    21 -> org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    else -> throw GradleException("Unsupported Kotlin JVM target for Java version: $javaVersion")
}


android {
    namespace = "com.exsite.meapmeap"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.exsite.meapmeap"
        minSdk = 30
        targetSdk = 36
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            setProperty("archivesBaseName", "MeapMeap-v${defaultConfig.versionName}")
        }
    }
    compileOptions {
        sourceCompatibility = javaVersionEnum
        targetCompatibility = javaVersionEnum
    }

    buildFeatures {
        compose = true
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(kotlinJvmTargetEnum)
        }
        jvmToolchain(javaVersion)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}