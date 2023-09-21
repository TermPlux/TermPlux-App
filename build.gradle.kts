buildscript {
    dependencies {
        classpath(dependencyNotation = "com.google.gms:google-services:4.4.0")
        classpath(dependencyNotation = "androidx.navigation:navigation-safe-args-gradle-plugin:2.7.2")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.android.library") version "8.1.1" apply false
    id("dev.rikka.tools.refine") version "4.3.0" apply false
    id("dev.rikka.tools.materialthemebuilder") version "1.3.3" apply false
}