plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "io.termplux.hybrid"
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
    implementation(rootProject.findProject(":flutter_boost") ?: error("flutter_boost"))
    implementation(rootProject.findProject(":flutter_plugin_android_lifecycle") ?: error("flutter_plugin_android_lifecycle"))
    implementation(project(":flutter"))
    implementation(project(":framework"))
    implementation(project(":common"))
    implementation(libs.androidx.fragment.ktx)
}