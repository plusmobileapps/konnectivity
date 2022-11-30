package com.plusmobileapps.konnectivity.sample

import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.observer

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
    object : Value<T>() {
        override val value: T get() = state
        private var disposables = emptyMap<(T) -> Unit, Disposable>()

        override fun subscribe(observer: (T) -> Unit) {
            val disposable: Disposable = states(observer(onNext = observer))
            this.disposables += observer to disposable
        }

        override fun unsubscribe(observer: (T) -> Unit) {
            val disposable = disposables[observer] ?: return
            this.disposables -= observer
            disposable.dispose()
        }
    }