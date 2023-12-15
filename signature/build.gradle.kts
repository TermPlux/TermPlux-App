plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "io.ecosed.signature"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "io.ecosed.framework"
                artifactId = "signature"
                pom {
                    name = "EcosedKit - framework - $artifactId"
                    description = artifactId
                    url = "https://github.com/ecosed/EcosedKit"
                    licenses {
                        license {
                            name = "Apache-2.0 License"
                            url = "https://github.com/ecosed/EcosedKit/blob/master/LICENSE"
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

    implementation(dependencyNotation = project(":aidl"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}