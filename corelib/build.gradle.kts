plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelightPlugin)
}

kotlin {
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
            baseName = "corelib"
            isStatic = true
        }
    }

    sourceSets {
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
                implementation(libs.sql)
                implementation(libs.colormath.compose)
                implementation(libs.koin.core)
                implementation(libs.kamel)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.datetime)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosX64Main by getting
        sourceSets["commonMain"].resources.srcDir("src/commonMain/resources")
    }
}

android {
    namespace = "com.mace.corelib"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
