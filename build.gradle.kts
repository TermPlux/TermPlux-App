buildscript {
    dependencies {
        classpath(dependencyNotation = "com.google.gms:google-services:4.3.15")
        classpath(dependencyNotation = "androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.0" apply false
}