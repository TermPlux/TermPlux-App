plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "io.termplux.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.termplux.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    implementation(libs.base.framework)
    implementation(libs.androidx.fragment)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    wearApp(project(":wear"))
    implementation(project(":base"))
    implementation(project(":hybrid"))
    implementation(project(":utils"))

    implementation(project(":launcher"))

    implementation(project(":ui"))

    implementation(libs.play.services.wearable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}