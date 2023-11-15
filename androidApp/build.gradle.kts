plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    namespace = "com.mace.kmpmacetemplate"
    compileSdk = 34
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

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
    }
}

dependencies {
//    implementation(projects.shared)
//    implementation(libs.compose.ui.tooling.preview)
//    implementation(libs.compose.material3)
//    debugImplementation(libs.compose.ui.tooling)
    implementation(projects.shared)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kermit)
//    implementation(libs.koin.core)
//    implementation(libs.koin.android)
//    implementation(libs.androidx.ui)
//    implementation(libs.compose.ui)
//    implementation(libs.compose.ui.tooling.preview)
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))

//    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose) {
        //exclude("org.jetbrains.compose.runtime", "runtime-desktop")
        exclude("androidx.compose.runtime", "runtime")
        exclude("androidx.compose.runtime", "runtime-saveable")
       // exclude("org.jetbrains.compose.runtime", "runtime-saveable-desktop")
        exclude("androidx.compose.ui", "ui")
       // exclude("org.jetbrains.compose.ui", "ui-desktop")
    }




//    implementation(libs.sql)
//    implementation(libs.colormath)
//    implementation(libs.colormath.jvm)
//    implementation(libs.colormath.compose)
//    implementation(libs.moko.resources)
//    implementation(libs.moko.resources.compose)
}