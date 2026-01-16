enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        jcenter()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/jetbrains/maven") }
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        jcenter()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/jetbrains/maven") }
        google()
        mavenCentral()
    }
}

rootProject.name = "KmpMACETemplate"
include(":androidApp")
include(":shared")
include(":corelib")
include(":mediaplayer")
include(":mediaplayerdata")
