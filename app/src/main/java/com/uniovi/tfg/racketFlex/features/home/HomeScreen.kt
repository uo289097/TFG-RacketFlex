package com.uniovi.tfg.racketFlex.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniovi.tfg.racketFlex.core.model.ClubModule

@Composable
fun HomeScreen(
    modules: List<ClubModule>,
    onReservasClick: () -> Unit,
    onPartidosClick: () -> Unit
) {

    Scaffold(
        bottomBar = {
            NavigationBar {

                if (modules.contains(ClubModule.RESERVAS)) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onReservasClick,
                        icon = { null },
                        label = { Text("Reservas") }
                    )
                }

                if (modules.contains(ClubModule.PARTIDOS)) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onPartidosClick,
                        icon = { null },
                        label = { Text("Partidos") }
                    )
                }

            }
        }
    ) { padding ->

        Box(Modifier.padding(padding)) {
            Text("Pantalla principal")
        }

    }
}