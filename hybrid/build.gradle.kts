plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.ecosed.hybrid"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(dependencyNotation = rootProject.findProject(":flutter_boost") ?: error("flutter_boost"))
    implementation(dependencyNotation = project(path = ":flutter"))
    implementation(dependencyNotation = project(path = ":engine"))
    implementation(dependencyNotation = project(path = ":plugin"))
   // implementation(dependencyNotation = project(path = ":bridge"))
}