buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
    dependencies {
        classpath(dependencyNotation = "com.android.tools.build:gradle:8.1.0-rc01")
        classpath(dependencyNotation = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath(dependencyNotation = "com.google.gms:google-services:4.3.15")
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}

subprojects {
    project.evaluationDependsOn(":flutter")
}