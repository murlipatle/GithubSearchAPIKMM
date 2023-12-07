plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.21"
    id("com.squareup.sqldelight").version("1.5.5")

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
    val sqlDelightVersion = "1.5.5"
    val dateTimeVersion = "0.4.1"
    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")

        }
    }
}

android {
    namespace = "com.example.githubapp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
sqldelight {
    database("GitDatabase") {
        packageName = "com.example.githubapp.db"
    }
}