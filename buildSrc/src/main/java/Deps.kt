object Deps {
    const val LIBRARY_VERSION = "0.1-alpha01"
    object Jetbrains {
        const val KOTLIN_VERSION = "1.7.20"
        const val COROUTINES_VERSION = "1.6.4"

        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION"
        const val coroutinesTesting =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION"
    }

    object Android {
        const val compileSDK = 33
        const val minSDK = 21
        const val targetSDK = 33

        const val androidGradlePlugin = "com.android.tools.build:gradle:7.3.1"
        const val startUp = "androidx.startup:startup-runtime:1.1.1"
    }

    object Compose {
        const val VERSION = "1.3.1"
        const val COMPILER_VERSION = "1.3.2"

        const val ui = "androidx.compose.ui:ui:$VERSION"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$VERSION"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$VERSION"
        const val foundation = "androidx.compose.foundation:foundation:$VERSION"
        const val material = "androidx.compose.material:material:$VERSION"
        const val activity = "androidx.activity:activity-compose:1.6.1"
    }

    object ArkIvanov {
        object MVIKotlin {
            private const val VERSION = "3.0.2"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mviKotlinExtensionsCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$VERSION"
        }

        object Decompose {
            private const val VERSION = "1.0.0-beta-01"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetpack:$VERSION"
        }

        object Essenty {
            private const val VERSION = "0.6.0"
            const val lifecycle = "com.arkivanov.essenty:lifecycle:$VERSION"
        }
    }
}