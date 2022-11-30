package com.plusmobileapps.konnectivity.sample

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import com.plusmobileapps.konnectivity.sample.KonnectivityStatusStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class KonnectivityStatusStoreProvider(
    private val storeFactory: StoreFactory,
    private val mainDispatcher: CoroutineDispatcher,
    private val konnectivity: Konnectivity,
) {
    sealed class Message {
        data class NetworkConnectionUpdated(val networkConnection: NetworkConnection) : Message()
    }

    fun provide(): KonnectivityStatusStore =
        object : KonnectivityStatusStore, Store<Nothing, State, Nothing> by storeFactory.create(
            name = "KonnectivityStatusStore",
            initialState = State(networkConnection = konnectivity.currentNetworkConnection),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Nothing, Unit, State, Message, Nothing>(
        mainContext = mainDispatcher
    ) {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                konnectivity.currentNetworkConnectionState.collect {
                    dispatch(Message.NetworkConnectionUpdated(it))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State = when (msg) {
            is Message.NetworkConnectionUpdated -> copy(networkConnection = msg.networkConnection)
        }
    }
}