plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        compileSdk = 36
    }
}

kotlin {

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.content.negotiation)
        }
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)
            implementation(libs.koin.core)
        }
        iosMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.darwin)
        }
    }
}
