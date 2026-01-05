import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelightPlugin)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            binaryOption("bundleId", "shared")
            export(projects.corelib)
            export(projects.mediaplayer)
        }
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.sql.android)
                implementation(libs.koin.core)
                implementation(libs.koin.android)
                implementation(libs.kermit)
                api(projects.corelib)
                api(projects.mediaplayer)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.ktor.client.darwin)
                implementation(libs.sql.ios)
                api(projects.corelib)
                api(projects.mediaplayer)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlin.reflect)
                implementation(libs.androidx.material.icons)
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.colormath.compose)
                implementation(libs.kermit)
                implementation(libs.kmm.viewmodel)
                implementation(libs.coroutines.core)
                implementation(libs.precompose.navigation)
                implementation(libs.kotlin.serialization)
                implementation(libs.ktor.client.core)
                implementation(libs.sql)
                implementation(libs.colormath.compose)
                implementation(libs.koin.core)
                implementation(libs.kamel)
                implementation(libs.kamel.decoder)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.datetime)
                api(projects.corelib)
                api(projects.mediaplayer)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosX64Main by getting
        //sourceSets["commonMain"].resources.srcDir("src/commonMain/resources")
    }
}

kotlin {
    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
    androidLibrary {
        namespace = "com.example.namespace"
        compileSdk = 36
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.jetbrains.handson.kmm.shared.cache"
    }
}

private fun KotlinMultiplatformExtension.iosTarget(function: Any) {}
private fun KotlinMultiplatformExtension.framework(function: Any) {}
private fun KotlinMultiplatformExtension.binaryOption(function: Any, string: String) {}
