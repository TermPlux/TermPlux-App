pluginManagement {
    val storageUrl: String = System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "repo")
        maven(url = "$storageUrl/download.flutter.io")
        mavenLocal()
    }
}
dependencyResolutionManagement {
    val storageUrl: String = System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "repo")
        maven(url = "$storageUrl/download.flutter.io")
        mavenLocal()
    }
}

rootProject.name = "EcosedDroid"
include(":demo", ":ecosed-droid")