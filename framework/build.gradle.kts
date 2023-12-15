plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "io.ecosed"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        aidl = true
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
                pom {
                    name = "EcosedKit - framework - $artifactId"
                    description = artifactId
                    url = "https://github.com/ecosed/framework"
                    licenses {
                        license {
                            name = "Apache-2.0 License"
                            url = "https://github.com/ecosed/framework/blob/master/LICENSE"
                        }
                    }
                    developers {
                        developer {
                            name = "wyq0918dev"
                            url = "https://github.com/wyq0918dev"
                        }
                    }
                }
                from(components["release"])
            }
        }
    }
}

dependencies {
    // 本地依赖
    implementation(dependencyNotation = project(":engine"))
    implementation(dependencyNotation = project(":plugin"))
    // 第二方库
    implementation(dependencyNotation = "com.github.ecosed:common:0.0.1")
    // 第三方库
    // AndroidUtilCode: https://github.com/Blankj/AndroidUtilCode
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    // LicensesDialog: https://github.com/PSDev/LicensesDialog
    implementation(dependencyNotation = "de.psdev.licensesdialog:licensesdialog:2.2.0")

    // AndroidHiddenApiBypass: https://github.com/LSPosed/AndroidHiddenApiBypass
    implementation(dependencyNotation = "org.lsposed.hiddenapibypass:hiddenapibypass:4.3")

    // Google 官方库
    implementation(dependencyNotation = "androidx.annotation:annotation:1.7.1")
    implementation(dependencyNotation = "androidx.browser:browser:1.7.0")
    implementation(dependencyNotation = "androidx.core:core-ktx:1.12.0")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

}