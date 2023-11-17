plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    namespace = "com.mace.kmpmacetemplate.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.mace.kmpmacetemplate.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
        kotlin {
            kotlinOptions {
                freeCompilerArgs += "-Xexpect-actual-classes"
            }
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kermit)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.kmm.viewmodel.lifecycle)
    implementation(libs.kmm.viewmodel)

//    implementation(libs.androidx.activity.compose) {
//        //exclude("org.jetbrains.compose.runtime", "runtime-desktop")
//        exclude("androidx.compose.runtime", "runtime")
//        exclude("androidx.compose.runtime", "runtime-saveable")
//       // exclude("org.jetbrains.compose.runtime", "runtime-saveable-desktop")
//        exclude("androidx.compose.ui", "ui")
//       // exclude("org.jetbrains.compose.ui", "ui-desktop")
//    }
}