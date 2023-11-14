plugins {
    alias(libs.plugins.androidApplication)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
//    implementation(projects.shared)
//    implementation(libs.compose.ui)
//    implementation(libs.compose.ui.tooling.preview)
//    implementation(libs.compose.material3)
//    implementation(libs.androidx.activity.compose)
//    debugImplementation(libs.compose.ui.tooling)
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.android)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}