pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "repo")
        maven(url = "${System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"}/download.flutter.io")
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
        maven(url = "repo")
        maven(url = "${System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"}/download.flutter.io")
        mavenLocal()
    }
}

rootProject.name = "framework"
include(":demo", ":framework")
include(":engine")
