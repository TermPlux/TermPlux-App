plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("maven-publish")
}

android {
    namespace = "io.ecosed.libecosed"
    compileSdk = 33

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
        compose = true
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
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

configurations.all {
    exclude(
        group = "androidx.appcompat",
        module = "appcompat"
    )
}

dependencies {

    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    implementation(dependencyNotation = "com.github.ecosed:plugin:3.1.0")
    implementation(dependencyNotation = "com.github.farmerbb:libtaskbar:2.2.0")
    implementation(dependencyNotation = "de.psdev.licensesdialog:licensesdialog:2.2.0")
    implementation(dependencyNotation = "dev.rikka.shizuku:api:13.1.1")
    implementation(dependencyNotation = "dev.rikka.shizuku:provider:13.1.1")
    implementation(dependencyNotation = "org.lsposed.hiddenapibypass:hiddenapibypass:4.3")

    implementation(dependencyNotation = "dev.rikka.rikkax.appcompat:appcompat:1.6.1")
    implementation(dependencyNotation = "dev.rikka.rikkax.buildcompat:buildcompat:34.0.1")
    implementation(dependencyNotation = "dev.rikka.rikkax.core:core-ktx:1.4.1")
    implementation(dependencyNotation = "dev.rikka.rikkax.insets:insets:1.3.0")
    implementation(dependencyNotation = "dev.rikka.rikkax.layoutinflater:layoutinflater:1.3.0")
    implementation(dependencyNotation = "dev.rikka.rikkax.material:material:2.7.0")
    implementation(dependencyNotation = "dev.rikka.rikkax.material:material-preference:2.0.0")
    implementation(dependencyNotation = "dev.rikka.rikkax.parcelablelist:parcelablelist:2.0.1")
    implementation(dependencyNotation = "dev.rikka.rikkax.recyclerview:recyclerview-ktx:1.3.1")
    implementation(dependencyNotation = "dev.rikka.rikkax.widget:borderview:1.1.0")
    implementation(dependencyNotation = "dev.rikka.rikkax.widget:mainswitchbar:1.0.2")



    implementation(dependencyNotation = "androidx.core:core-ktx:1.10.1")
    implementation(dependencyNotation = "androidx.appcompat:appcompat:1.6.1")
    implementation(dependencyNotation = "com.google.android.material:material:1.9.0")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(dependencyNotation = "androidx.activity:activity-compose:1.7.2")


    implementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    implementation(dependencyNotation = "androidx.compose.ui:ui")
    implementation(dependencyNotation = "androidx.compose.ui:ui-graphics")
    implementation(dependencyNotation = "androidx.compose.ui:ui-tooling-preview")
    implementation(dependencyNotation = "androidx.compose.material3:material3")
    implementation(dependencyNotation = "androidx.compose.material3:material3-window-size-class")
    implementation(dependencyNotation = "androidx.compose.material:material-icons-core")
    implementation(dependencyNotation = "androidx.compose.material:material-icons-extended")



    testImplementation(dependencyNotation = "junit:junit:4.13.2")
    androidTestImplementation(dependencyNotation = "androidx.test.ext:junit:1.1.5")
    androidTestImplementation(dependencyNotation = "androidx.test.espresso:espresso-core:3.5.1")



    androidTestImplementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-tooling")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-test-manifest")
}