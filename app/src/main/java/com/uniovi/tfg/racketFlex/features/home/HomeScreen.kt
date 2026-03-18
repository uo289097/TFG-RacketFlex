package com.uniovi.tfg.racketFlex.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen
import com.uniovi.tfg.racketFlex.features.matches.MatchesScreen

@Composable
fun HomeScreen(
    userId: String,
    clubId: String,
    modules: List<ClubModule>,
) {
    var selectedModule by remember { mutableStateOf(ClubModule.RESERVAS) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                modules.forEach { module ->
                    NavigationBarItem(
                        icon = { /* aquí tu icono */ },
                        label = { Text(module.name) },
                        selected = selectedModule == module,
                        onClick = { selectedModule = module }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedModule) {
                ClubModule.RESERVAS -> BookingScreen(clubId = clubId, userId = userId)
                ClubModule.PARTIDOS -> MatchesScreen()
                else -> Text("Módulo no implementado")
            }
        }
    }
}