plugins {
    id("com.android.application") version "8.5.1" apply (false)
    alias(libs.plugins.kotlinAndroid) apply (false)
    kotlin("multiplatform") version "2.3.0" apply false
    id("org.jetbrains.compose") version "1.6.11" apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.sqlDelightPlugin) apply false
}
