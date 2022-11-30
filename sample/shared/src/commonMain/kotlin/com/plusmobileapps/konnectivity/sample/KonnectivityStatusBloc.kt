package com.plusmobileapps.konnectivity.sample

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.plusmobileapps.konnectivity.Konnectivity
import kotlinx.coroutines.Dispatchers

interface KonnectivityStatusBloc {
    val models: Value<Model>

    data class Model(val currentNetworkStatus: String)
}

fun KonnectivityStatusBloc(context: ComponentContext): KonnectivityStatusBloc =
    KonnectivityStatusBlocImpl(
        context = context,
        mainDispatcher = Dispatchers.Main,
        storeFactory = DefaultStoreFactory(),
        konnectivity = Konnectivity(),
    )