plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dev.rikka.tools.refine")
    id("dev.rikka.tools.materialthemebuilder")
    id("maven-publish")
}

android {
    namespace = "io.ecosed.libecosed"
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
        compose = true
        buildConfig = true
        viewBinding = true
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

materialThemeBuilder {
    themes {
        create("LibEcosed"){
            primaryColor = "#D0BCFF"
            lightThemeFormat = "Theme.Material3.Light.%s"
            lightThemeParent = "Theme.Material3.Light.NoActionBar"
            darkThemeFormat = "Theme.Material3.Dark.%s"
            darkThemeParent = "Theme.Material3.Dark.NoActionBar"
        }
    }
    generatePalette = true
    generatePaletteAttributes = true
    generateTextColors = true
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
    // 第三方库
    // AndroidUtilCode: https://github.com/Blankj/AndroidUtilCode
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    // EcosedPlugin: https://github.com/ecosed/plugin
    implementation(dependencyNotation = "com.github.ecosed:plugin:6.1.0")
    // LibTaskbar: https://github.com/farmerbb/libtaskbar
    implementation(dependencyNotation = "com.github.farmerbb:libtaskbar:2.2.0")
    // DialogX: https://github.com/kongzue/DialogX
    implementation(dependencyNotation = "com.kongzue.dialogx:DialogX:0.0.48")
    implementation(dependencyNotation = "com.kongzue.dialogx.style:DialogXIOSStyle:0.0.48")
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
    // RikkaX: https://github.com/RikkaApps/RikkaX
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
    // JetBrains 官方库
    implementation(dependencyNotation = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.0")
    implementation(dependencyNotation = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    // Google 官方库
    implementation(dependencyNotation = "androidx.core:core-ktx:1.10.1")
    implementation(dependencyNotation = "androidx.annotation:annotation:1.6.0")
    implementation(dependencyNotation = "androidx.fragment:fragment-ktx:1.6.1")
    implementation(dependencyNotation = "androidx.preference:preference-ktx:1.2.1")
    implementation(dependencyNotation = "androidx.viewpager2:viewpager2:1.0.0")
    implementation(dependencyNotation = "androidx.recyclerview:recyclerview:1.3.1")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation(dependencyNotation = "androidx.browser:browser:1.6.0")
    implementation(dependencyNotation = "androidx.activity:activity-compose:1.7.2")
    implementation(dependencyNotation = "com.google.android.gms:play-services-base:18.2.0")
    implementation(dependencyNotation = "com.google.accompanist:accompanist-webview:0.32.0")
    implementation(dependencyNotation = "com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation(dependencyNotation = "com.google.accompanist:accompanist-adaptive:0.32.0")
    implementation(dependencyNotation = "com.google.accompanist:accompanist-drawablepainter:0.32.0")
    implementation(dependencyNotation = "androidx.navigation:navigation-compose:2.7.1")
    implementation(dependencyNotation = "androidx.navigation:navigation-ui-ktx:2.7.1")
    implementation(dependencyNotation = "androidx.core:core-ktx:1.10.1")
    implementation(dependencyNotation = "androidx.appcompat:appcompat:1.6.1")
    implementation(dependencyNotation = "com.google.android.material:material:1.9.0")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(dependencyNotation = "androidx.activity:activity-compose:1.7.2")
    implementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    implementation(dependencyNotation = "androidx.compose.ui:ui:1.5.0")
    implementation(dependencyNotation = "androidx.compose.ui:ui-graphics:1.5.0")
    implementation(dependencyNotation = "androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation(dependencyNotation = "androidx.compose.ui:ui-viewbinding:1.5.0")
    implementation(dependencyNotation = "androidx.compose.material3:material3:1.1.1")
    implementation(dependencyNotation = "androidx.compose.material3:material3-window-size-class:1.1.1")
    implementation(dependencyNotation = "androidx.compose.material:material-icons-core:1.5.0")
    implementation(dependencyNotation = "androidx.compose.material:material-icons-extended:1.5.0")
    // 测试和调试
    testImplementation(dependencyNotation = "junit:junit:4.13.2")
    androidTestImplementation(dependencyNotation = "androidx.test.ext:junit:1.1.5")
    androidTestImplementation(dependencyNotation = "androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(dependencyNotation = platform(notation = "androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    androidTestImplementation(dependencyNotation = "androidx.navigation:navigation-testing:2.7.1")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-tooling:1.5.0")
    debugImplementation(dependencyNotation = "androidx.compose.ui:ui-test-manifest:1.5.0")
}