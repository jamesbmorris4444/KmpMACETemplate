
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
            implementation(compose.preview)
            implementation(libs.kotlin.stdlib)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.session)
            implementation(libs.androidx.media3.ui)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            api(project(":mediaplayerdata"))
            implementation(libs.kotlin.stdlib)
            implementation(libs.androidx.material.icons)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.napier)
//            implementation(libs.androidx.lifecycle.runtime.compose)
//            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)
            implementation(libs.koin.core)
            implementation(libs.navigation.compose)
            implementation(libs.image.loader)

        }
        iosMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.koin.core)
        }
//        val desktopMain by getting
//        desktopMain.dependencies {
//            implementation(libs.compose.desktop)
//        }
    }
}
