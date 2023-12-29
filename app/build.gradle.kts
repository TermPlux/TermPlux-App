plugins {
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.kotlinAndroid)
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
}

dependencies {
    wearApp(dependencyNotation = project(":wear"))
    implementation(dependencyNotation = project(path = ":hybrid"))

    implementation(project(":utils"))

    implementation(project(":base"))

    implementation(libs.play.services.wearable)
    implementation(dependencyNotation = libs.androidx.core.ktx)
    implementation(dependencyNotation = libs.androidx.appcompat)
    implementation(dependencyNotation = libs.material)
    implementation(dependencyNotation = libs.androidx.constraintlayout)
}