pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}
rootProject.name = "TermPlux-App"
apply(from = "flutter.gradle")
include(":stub")
include(":app")
include(":wear")
//include(":framework")
include(":engine")
include(":plugin")
include(":nativelib")
include(":client")
include(":provider")
include(":aidl")
include(":signature")
//include(":common")
//include(":bridge")
include(":hybrid")
include(":utils")
