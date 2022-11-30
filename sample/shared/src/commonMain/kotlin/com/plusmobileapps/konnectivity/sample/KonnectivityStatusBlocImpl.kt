package com.plusmobileapps.konnectivity.sample

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import com.plusmobileapps.konnectivity.sample.KonnectivityStatusBloc.Model
import kotlinx.coroutines.CoroutineDispatcher

internal class KonnectivityStatusBlocImpl(
    context: ComponentContext,
    mainDispatcher: CoroutineDispatcher,
    storeFactory: StoreFactory,
    konnectivity: Konnectivity,
) : KonnectivityStatusBloc, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        KonnectivityStatusStoreProvider(storeFactory, mainDispatcher, konnectivity).provide()
    }

    override val models: Value<Model> = store.asValue().map {
        Model(
            currentNetworkStatus = when (it.networkConnection) {
                NetworkConnection.NONE -> "Not connected to the internet"
                NetworkConnection.WIFI -> "Connected to wifi"
                NetworkConnection.CELLULAR -> "Connected to cellular"
            }
        )
    }
}