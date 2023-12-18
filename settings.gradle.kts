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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}

rootProject.name = "framework"
include(":demo")
include(":framework")
include(":engine")
include(":plugin")
include(":nativelib")
include(":client")
include(":provider")
include(":aidl")
include(":signature")
include(":common")
include(":bridge")
