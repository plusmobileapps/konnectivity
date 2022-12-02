pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Konnectivity"
include(":sample:androidApp")
include(":sample:shared")
include(":konnectivity")
includeBuild("convention-plugins")