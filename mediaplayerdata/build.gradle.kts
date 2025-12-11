import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    androidTarget()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

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
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
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
            implementation(libs.koin.core)
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.vditital.data"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }
}
