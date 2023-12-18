plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "io.ecosed.engine"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
    implementation(dependencyNotation = project(":plugin"))
    implementation(dependencyNotation = project(":client"))
    implementation(dependencyNotation = project(":common"))
    implementation(dependencyNotation = "com.blankj:utilcodex:1.31.1")
    implementation(dependencyNotation = "androidx.lifecycle:lifecycle-common:2.6.2")
    implementation(dependencyNotation = "androidx.appcompat:appcompat:1.6.1")
}