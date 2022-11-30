package com.plusmobileapps.konnectivity

import com.plusmobileapps.konnectivity.NetworkConnection.NONE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

interface Konnectivity {
    val isConnected: Boolean
    val currentNetworkConnection: NetworkConnection
    val isConnectedState: StateFlow<Boolean>
    val currentNetworkConnectionState: StateFlow<NetworkConnection>
}

expect fun Konnectivity(): Konnectivity

internal class KonnectivityImpl(
    initialConnection: NetworkConnection = NONE,
    ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Konnectivity {

    private val scope = CoroutineScope(ioDispatcher)

    private val state = MutableStateFlow<NetworkConnection>(initialConnection)

    override val isConnected: Boolean
        get() = state.value != NONE

    override val currentNetworkConnection: NetworkConnection
        get() = state.value

    override val isConnectedState: StateFlow<Boolean> =
        state.asStateFlow()
            .map(scope) { it != NONE }

    override val currentNetworkConnectionState: StateFlow<NetworkConnection> = state.asStateFlow()

    fun onNetworkConnectionChanged(connection: NetworkConnection) {
        state.value = connection
    }

    private fun <T, M> StateFlow<T>.map(
        coroutineScope : CoroutineScope,
        mapper : (value : T) -> M
    ) : StateFlow<M> = map { mapper(it) }.stateIn(
        coroutineScope,
        SharingStarted.Eagerly,
        mapper(value)
    )
}