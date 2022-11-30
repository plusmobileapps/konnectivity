package com.plusmobileapps.konnectivity.sample

import com.arkivanov.mvikotlin.core.store.Store
import com.plusmobileapps.konnectivity.NetworkConnection
import com.plusmobileapps.konnectivity.sample.KonnectivityStatusStore.State

internal interface KonnectivityStatusStore : Store<Nothing, State, Nothing> {
    data class State(val networkConnection: NetworkConnection = NetworkConnection.NONE)
}