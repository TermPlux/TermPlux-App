plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "io.ecosed.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.ecosed.example"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(dependencyNotation = project(path = ":framework"))
    implementation(dependencyNotation = "com.github.ecosed:common:1.0.0")

    implementation(dependencyNotation = "com.google.android.material:material:1.10.0")
    implementation(dependencyNotation = "androidx.core:core-ktx:1.12.0")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(dependencyNotation = "androidx.activity:activity-compose:1.8.1")
    implementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    implementation(dependencyNotation = "androidx.compose.ui:ui:1.5.4")
    implementation(dependencyNotation = "androidx.compose.ui:ui-graphics:1.5.4")
    implementation(dependencyNotation = "androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation(dependencyNotation = "androidx.compose.material3:material3:1.1.2")
    testImplementation(dependencyNotation = "junit:junit:4.13.2")
    androidTestImplementation(dependencyNotation = "androidx.test.ext:junit:1.1.5")
    androidTestImplementation(dependencyNotation = "androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(dependencyNotation = "androidx.compose.ui:ui-test-junit4:1.5.4")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-tooling:1.5.4")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-test-manifest:1.5.4")
}