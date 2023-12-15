plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dev.rikka.tools.refine")
    id("maven-publish")
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

    implementation(project(":plugin"))
    implementation(project(":aidl"))
    implementation(project(":nativelib"))
    implementation(project(":signature"))


    // Shizuku-API: https://github.com/RikkaApps/Shizuku-API
    implementation(dependencyNotation = "dev.rikka.shizuku:api:13.1.5")
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    // HiddenApiRefinePlugin: https://github.com/RikkaApps/HiddenApiRefinePlugin
    implementation(dependencyNotation = "dev.rikka.tools.refine:annotation:4.3.0")
    implementation(dependencyNotation = "dev.rikka.tools.refine:runtime:4.3.0")
    kapt(dependencyNotation = "dev.rikka.tools.refine:annotation-processor:4.3.0")

    implementation(dependencyNotation = "com.google.android.gms:play-services-base:18.2.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

}