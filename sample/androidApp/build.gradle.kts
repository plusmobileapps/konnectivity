plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.plusmobileapps.konnectivity.android"
    compileSdk = Deps.Android.compileSDK
    defaultConfig {
        applicationId = "com.plusmobileapps.konnectivity.android"
        minSdk = Deps.Android.minSDK
        targetSdk = Deps.Android.targetSDK
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Compose.COMPILER_VERSION
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.uiTooling)
    implementation(Deps.Compose.uiToolingPreview)
    implementation(Deps.Compose.material)
    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.activity)
    implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
}