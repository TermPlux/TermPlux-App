plugins {
    alias(notation = libs.plugins.androidLibrary)
    alias(notation = libs.plugins.kotlinAndroid)
    alias(notation = libs.plugins.kotlinKapt)
    alias(notation = libs.plugins.rikkaToolsRefine)
}

android {
    namespace = "io.ecosed.client"
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

    implementation(dependencyNotation = project(path = ":plugin"))
    implementation(dependencyNotation = project(path = ":aidl"))
    implementation(dependencyNotation = project(path = ":nativelib"))
    implementation(dependencyNotation = project(path = ":provider"))
    implementation(dependencyNotation = project(path = ":signature"))


    // Shizuku-API: https://github.com/RikkaApps/Shizuku-API
    implementation(dependencyNotation = "dev.rikka.shizuku:api:13.1.5")
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    // HiddenApiRefinePlugin: https://github.com/RikkaApps/HiddenApiRefinePlugin
    implementation(dependencyNotation = "dev.rikka.tools.refine:annotation:4.3.0")
    implementation(dependencyNotation = "dev.rikka.tools.refine:runtime:4.3.0")

    kapt(dependencyNotation = "dev.rikka.tools.refine:annotation-processor:4.3.0")

    implementation(dependencyNotation = "com.google.android.gms:play-services-base:18.2.0")

    implementation(dependencyNotation = libs.androidx.core.ktx)
    implementation(dependencyNotation = libs.androidx.appcompat)
    implementation(dependencyNotation = libs.material)

}