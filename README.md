# Konnectivity

A Kotlin multiplaform mobile library for checking the network connectivity status of a mobile device.

## Setup

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
}
```

Add Konnectivity to `commonMain` dependencies with the latest version.

[![Maven Central](https://img.shields.io/maven-central/v/com.plusmobileapps/konnectivity?color=blue)](https://search.maven.org/artifact/com.plusmobileapps/konnectivity)


```kotlin
kotlin {
    android()
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.plusmobileapps:konnectivity:$version")
            }
        }
    }
}
```

## Usage

Create a single instance of `Konnectivity` and inject into your app.

```kotlin
// create a single instance
val konnectivity: Konnectivity = Konnectivity()
```

Retrieve the current value of the network connectivity status. 

```kotlin
val isConnected: Boolean = konnectivity.isConnected

val networkConnection: NetworkConnection = konnectivity.currentNetworkConnection
when (networkConnection) {
    NetworkConnection.NONE -> "Not connected to the internet"
    NetworkConnection.WIFI -> "Connected to wifi"
    NetworkConnection.CELLULAR -> "Connected to cellular"
}
```

Observe the latest value of the network connectivity status. Replace `GlobalScope` with your own `CoroutineScope`.

```kotlin
GlobalScope.launch {
    konnectivity.isConnectedState.collect { isConnected -> 
       // insert code
    }
}

GlobalScope.launch {
    konnectivity.currentNetworkConnectionState.collect { connection -> 
        when (connection) {
            NetworkConnection.NONE -> "Not connected to the internet"
            NetworkConnection.WIFI -> "Connected to wifi"
            NetworkConnection.CELLULAR -> "Connected to cellular"
        }
    }
}
```

## Resources

* [Publishing your Kotlin Multiplatform library to Maven Central](https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k)