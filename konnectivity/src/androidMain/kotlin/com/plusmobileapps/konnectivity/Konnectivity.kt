package com.plusmobileapps.konnectivity

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

actual fun Konnectivity(): Konnectivity {
    val appContext = appContext!!
    val connectivityManager: ConnectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val konnectivity = KonnectivityImpl(
        initialConnection = getCurrentNetworkConnection(connectivityManager)
    )
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                konnectivity.onNetworkConnectionChanged(
                    getNetworkConnection(connectivityManager, network)
                )
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val connection = getNetworkConnection(networkCapabilities)
            konnectivity.onNetworkConnectionChanged(connection)
        }

        override fun onLost(network: Network) {
            konnectivity.onNetworkConnectionChanged(NetworkConnection.NONE)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    } else {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    return konnectivity
}

private fun getCurrentNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        postAndroidMNetworkConnection(connectivityManager)
    } else {
        preAndroidMNetworkConnection(connectivityManager)
    }

@TargetApi(Build.VERSION_CODES.M)
private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection {
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return getNetworkConnection(capabilities)
}

@Suppress("DEPRECATION")
private fun preAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
    when (connectivityManager.activeNetworkInfo?.type) {
        null -> NetworkConnection.NONE
        ConnectivityManager.TYPE_WIFI -> NetworkConnection.WIFI
        else -> NetworkConnection.CELLULAR
    }

private fun getNetworkConnection(
    connectivityManager: ConnectivityManager,
    network: Network
): NetworkConnection {
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return getNetworkConnection(capabilities)
}

private fun getNetworkConnection(capabilities: NetworkCapabilities?): NetworkConnection =
    when {
        capabilities == null -> NetworkConnection.NONE
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> NetworkConnection.NONE
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) -> NetworkConnection.NONE
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnection.CELLULAR
        else -> NetworkConnection.NONE
    }