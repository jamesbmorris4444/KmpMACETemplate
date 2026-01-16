plugins {
//    id("com.android.application")
//    kotlin("multiplatform")
//    id("org.jetbrains.compose")
//    kotlin("plugin.compose")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

kotlin {
//    androidTarget()
//
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "androidApp"
//            isStatic = true
//            binaryOption("bundleId", "com.jbm.maincmp")
//        }
//    }

    dependencies {
        implementation(libs.koin.android)
        implementation("androidx.appcompat:appcompat:1.6.1") // Add if not present elsewhere
        implementation("androidx.core:core-ktx:1.12.0") // Add if not present elsewhere
        implementation(projects.shared)
        implementation(libs.kotlin.stdlib)
        implementation(libs.compose.ui)
        implementation(libs.compose.ui.tooling.preview)
//        implementation(compose.material3)
        implementation(libs.androidx.activity.compose)
        implementation(libs.koin.core)
        implementation(libs.koin.android)
        implementation(libs.kermit)
        implementation(libs.kmm.viewmodel.lifecycle)
        implementation(libs.kmm.viewmodel)
        implementation(projects.corelib)
        implementation(projects.mediaplayer)
    }

    //remove expect actual warning
//    targets.configureEach {
//        compilations.configureEach {
//            compileTaskProvider.configure{
//                compilerOptions {
//                    freeCompilerArgs.add("-Xexpect-actual-classes")
//                }
//            }
//        }
//    }
}

android {
    namespace = "com.jbm.maincmp"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}