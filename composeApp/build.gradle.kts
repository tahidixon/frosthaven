import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    kotlin("native.cocoapods") version "2.0.0"
}

kotlin {
    cocoapods {
        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "ComposeApp"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "ComposeApp"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "ComposeApp"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            //isStatic = false
            transitiveExport = false // This is default.
            // Bitcode embedding
            //embedBitcode(BITCODE)
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FrosthavenHelper" // ToDo change where needed
            isStatic = true
        }
    }
    
    sourceSets {
        // ToDo TOML
        val ktorVersion = "2.3.12"
        val coroutinesVersion = "1.8.1"
        val koinVersion = "3.2.0"
        val voyagerVersion = "1.1.0-beta02"
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Ktor
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Kotlinx
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

            // Ktor
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

            // Koin
            implementation("io.insert-koin:koin-core:$koinVersion")
            implementation("io.insert-koin:koin-test:$koinVersion")

            // Navigator
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")

            // Screen Model
            implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")

            // BottomSheetNavigator
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")

            // TabNavigator
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")

            // Transitions
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

            // Koin integration
            implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")

            // Kermit
            implementation("co.touchlab:kermit:2.0.4")
            implementation("co.touchlab:stately-common:2.0.4")

            // Icon pack
            implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")

            // Swipe Box
            // https://github.com/KevinnZou/compose-swipebox-multiplatform
            implementation("io.github.kevinnzou:compose-swipebox-multiplatform:1.2.0")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }
    }
}

android {
    namespace = "com.tahidixon.frosthavenhelper" // ToDo change where needed
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.tahidixon.frosthavenhelper" // ToDo change where needed
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

