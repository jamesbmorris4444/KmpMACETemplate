plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization").version("1.9.10")
    id("com.squareup.sqldelight").version("1.5.5")
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
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
        val androidMain by getting {
            dependencies {
//                implementation(projects.androidApp)
//                implementation(libs.compose.ui)
//                implementation(libs.compose.ui.tooling.preview)
//                implementation(libs.androidx.activity.compose)
//                implementation(libs.ktor.client.android)
                implementation(libs.sql.android)
//                implementation(libs.koin.core)
//                implementation(libs.koin.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
                implementation(libs.sql.ios)
                implementation(libs.koin.core)
            }
        }
        val commonMain by getting {
            dependencies {
//                implementation(compose.runtime)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                implementation(compose.animation)
//
//                implementation(compose.components.resources)
                implementation(libs.precompose.navigation)
                implementation(libs.kermit)
                implementation(libs.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content)
                implementation(libs.ktor.serialization)
                implementation(libs.sql)
                implementation(libs.colormath)
                implementation(libs.colormath.compose)
                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)
                implementation(libs.kmm.viewmodel)
                implementation(libs.koin.core)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosX64Main by getting
//        val commonMain by getting {
//            dependencies {
//                implementation(compose.runtime)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                implementation(compose.animation)
//                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//                implementation(compose.components.resources)
//                implementation(libs.androidx.activity.compose)
////                implementation(libs.compose.ui)
////                implementation(libs.compose.ui.tooling)
////                implementation(libs.compose.ui.tooling.preview)
////                implementation(libs.compose.foundation)
//                implementation(libs.compose.material3)
//            }
//        }
//        val commonTest by getting {
//            dependencies {
//                implementation(libs.kotlin.test)
//            }
//        }
    }
}

android {
    namespace = "com.mace.kmpmacetemplate"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.jetbrains.handson.kmm.shared.cache"
    }
}
