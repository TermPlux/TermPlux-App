buildscript {
    dependencies {
        classpath(dependencyNotation = "com.google.gms:google-services:4.3.15")
        classpath(dependencyNotation = "androidx.navigation:navigation-safe-args-gradle-plugin:2.7.1")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.1" apply false
    id("dev.rikka.tools.refine") version "4.3.0" apply false
    id("dev.rikka.tools.materialthemebuilder") version "1.3.3" apply false
}