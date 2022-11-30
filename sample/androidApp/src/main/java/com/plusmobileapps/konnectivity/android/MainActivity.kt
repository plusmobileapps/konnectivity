package com.plusmobileapps.konnectivity.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.plusmobileapps.konnectivity.sample.KonnectivityStatusBloc

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bloc = KonnectivityStatusBloc(defaultComponentContext())
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    KonnectivityStatusContent(bloc = bloc)
                }
            }
        }
    }
}

@Composable
fun KonnectivityStatusContent(bloc: KonnectivityStatusBloc) {
    val model by bloc.models.subscribeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Current network status:",
            style = MaterialTheme.typography.h5
        )

        Text(
            text = model.currentNetworkStatus,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        KonnectivityStatusContent(
            object : KonnectivityStatusBloc {
                override val models: Value<KonnectivityStatusBloc.Model> = MutableValue(
                    KonnectivityStatusBloc.Model(
                        "Not currently connected to the internet"
                    )
                )
            }
        )
    }
}
