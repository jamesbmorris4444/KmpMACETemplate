enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        google()
        mavenCentral()
    }
}

rootProject.name = "KmpMACETemplate"
include(":androidApp")
include(":shared")
include(":corelib")
