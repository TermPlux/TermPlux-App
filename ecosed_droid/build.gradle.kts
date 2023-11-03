plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dev.rikka.tools.refine")
    id("maven-publish")
}

android {
    namespace = "io.ecosed.droid"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                abiFilters("armeabi-v7a", "arm64-v8a", "x86_64", "x86")
                arguments("-DANDROID_TOOLCHAIN=clang", "-DANDROID_STL=c++_static")
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file(path = "src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
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

    kotlin {
        jvmToolchain(11)
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}

dependencies {
    releaseImplementation("io.ecosed.droid:flutter_release:1.0")
    //profileImplementation("io.ecosed.mp_ecosed:flutter_profile:1.0")
    debugImplementation("io.ecosed.droid:flutter_debug:1.0")



    // 第三方库
    // AndroidUtilCode: https://github.com/Blankj/AndroidUtilCode
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    // LicensesDialog: https://github.com/PSDev/LicensesDialog
    implementation(dependencyNotation = "de.psdev.licensesdialog:licensesdialog:2.2.0")
    // Shizuku-API: https://github.com/RikkaApps/Shizuku-API
    implementation(dependencyNotation = "dev.rikka.shizuku:api:13.1.4")
    implementation(dependencyNotation = "dev.rikka.shizuku:provider:13.1.4")
    // AndroidHiddenApiBypass: https://github.com/LSPosed/AndroidHiddenApiBypass
    implementation(dependencyNotation = "org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    // HiddenApiRefinePlugin: https://github.com/RikkaApps/HiddenApiRefinePlugin
    implementation(dependencyNotation = "dev.rikka.tools.refine:annotation:4.3.0")
    implementation(dependencyNotation = "dev.rikka.tools.refine:runtime:4.3.0")
    kapt(dependencyNotation = "dev.rikka.tools.refine:annotation-processor:4.3.0")
    // Google 官方库
    implementation(dependencyNotation = "androidx.annotation:annotation:1.7.0")
    implementation(dependencyNotation = "androidx.browser:browser:1.6.0")
    implementation(dependencyNotation = "androidx.core:core-ktx:1.12.0")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(dependencyNotation = "com.google.android.gms:play-services-base:18.2.0")
    // 测试和调试
    testImplementation(dependencyNotation = "junit:junit:4.13.2")
    androidTestImplementation(dependencyNotation = "androidx.test.ext:junit:1.1.5")
    androidTestImplementation(dependencyNotation = "androidx.test.espresso:espresso-core:3.5.1")
}