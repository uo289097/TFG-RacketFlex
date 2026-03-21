package com.uniovi.tfg.racketFlex.features.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel

import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginViewModel
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen
import com.uniovi.tfg.racketFlex.features.matches.MatchesScreen


@OptIn(ExperimentalMaterial3Api::class)         //TODO Preguntar si es válido
@Composable
fun HomeScreen(
    user: User,
    modules: List<ClubModule>,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
) {
    var selectedModule by remember { mutableStateOf(ClubModule.RESERVAS) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(user.club) },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        loginViewModel.resetState()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                modules.forEach { module ->
                    NavigationBarItem(
                        icon = { /* TODO ICON */ },
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
                ClubModule.RESERVAS -> BookingScreen(clubId = user.club, userId = user.email)
                ClubModule.MATCHES -> MatchesScreen()
                else -> Text("Módulo no implementado")
            }
        }
    }
}