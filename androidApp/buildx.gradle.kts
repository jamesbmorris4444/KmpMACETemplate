plugins {
    alias(libs.plugins.androidApplication)
//    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.mace.kmpmacetemplate"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.mace.kmpmacetemplate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
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

dependencies {
    implementation(projects.shared)
    implementation(libs.kotlin.stdlib)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kermit)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.kmm.viewmodel.lifecycle)
    implementation(libs.kmm.viewmodel)
    implementation(projects.corelib)
    implementation(projects.mediaplayer)
}