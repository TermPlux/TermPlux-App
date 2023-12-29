plugins {
    alias(notation = libs.plugins.androidLibrary)
    alias(notation = libs.plugins.kotlinAndroid)
    alias(notation = libs.plugins.kotlinKapt)
    alias(notation = libs.plugins.rikkaToolsRefine)
}

android {
    namespace = "io.termplux.client"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        buildConfig = true
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
    implementation(project(":plugin"))
    implementation(project(":aidl"))
    implementation(project(":nativelib"))
    implementation(project(":provider"))
    implementation(project(":signature"))
    implementation(project(":utils"))
    implementation(libs.shizuku.api)
    implementation(libs.utilcodex)
    implementation(libs.refine.annotation)
    implementation(libs.refine.runtime)
    kapt(libs.refine.processor)
    implementation(libs.play.services.base)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}