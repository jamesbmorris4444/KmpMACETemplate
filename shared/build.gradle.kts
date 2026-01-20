import app.cash.sqlite.migrations.Database
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.kotlin.dsl.sqldelight

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    alias(libs.plugins.kotlinSerialization)
//    alias(libs.plugins.sqlDelightPlugin)
    alias(libs.plugins.sqlDelightPlugin)
}

android {
    namespace = "com.sqldb"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        compileSdk = 36
    }
}

kotlin {
//    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
//    applyDefaultHierarchyTemplate()
    
    androidTarget()

    jvmToolchain(17)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

//    sourceSets.all {
//        languageSettings.enableLanguageFeature("ExplicitBackingFields")
//    }



    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sql.android)
            implementation(libs.koin.android)
            api(projects.corelib)
            api(projects.mediaplayer)
        }
        iosMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.ios)
            api(projects.corelib)
            api(projects.mediaplayer)
        }
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlin.reflect)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.androidx.material.icons)
            implementation(libs.sql.coroutines.extensions)
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

//        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
//        val iosX64Main by getting
//        sourceSets["commonMain"].resources.srcDir("src/commonMain/resources")
    }

    sqldelight {
        databases {
            create("AppDatabase") {
                packageName.set("com.sqldb.database")
            }
        }
    }
}

//kotlin {
//    sourceSets {
//        all {
//            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
//        }
//    }
//}

//sqldelight {
//    database("AppDatabase") {
//        packageName = "com.jetbrains.handson.kmm.shared.cache"
//    }
//}

