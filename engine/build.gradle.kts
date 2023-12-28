plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.ecosed.engine"
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
    implementation(dependencyNotation = project(path = ":plugin"))
    implementation(dependencyNotation = project(path = ":client"))
    implementation(dependencyNotation = project(path = ":common"))
    implementation(dependencyNotation = libs.utilcodex)
    implementation(dependencyNotation = libs.androidx.lifecycle.common)
    implementation(dependencyNotation = libs.androidx.appcompat)
}