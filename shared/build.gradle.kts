plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelightPlugin)
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
        val androidMain by getting {
            dependencies {
                implementation(libs.sql.android)
                implementation(libs.koin.core)
                implementation(libs.koin.android)
                implementation(libs.kermit)
                implementation(libs.ktor.client.android)
                implementation(libs.kmm.viewmodel.lifecycle)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
                implementation(libs.sql.ios)
                implementation(libs.koin.core)
                implementation(libs.kermit)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
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
                implementation(libs.ktor.client.content)
                implementation(libs.ktor.client.engine)
                implementation(libs.sql)
                implementation(libs.colormath.compose)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosX64Main by getting
        sourceSets["commonMain"].resources.srcDir("src/commonMain/resources")
    }
}

android {
    namespace = "com.mace.kmpmacetemplate"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    sourceSets["main"].resources.srcDir("src/commonMain/resources")
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.jetbrains.handson.kmm.shared.cache"
    }
}
