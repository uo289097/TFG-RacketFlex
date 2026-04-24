package com.uniovi.tfg.racketFlex.features.home.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginViewModel
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen
import com.uniovi.tfg.racketFlex.features.courses.ui.CoursesScreen
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModel
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModelFactory
import com.uniovi.tfg.racketFlex.features.home.presentation.HomeViewModel
import com.uniovi.tfg.racketFlex.features.home.presentation.HomeViewModelFactory
import com.uniovi.tfg.racketFlex.features.home.ui.components.AboutDialog
import com.uniovi.tfg.racketFlex.features.home.ui.components.AccountDrawerSection
import com.uniovi.tfg.racketFlex.features.home.ui.components.AdminDrawerSection
import com.uniovi.tfg.racketFlex.features.home.ui.components.SupportDrawerSection
import com.uniovi.tfg.racketFlex.features.matches.ui.MatchesScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    onLogout: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToConfig: () -> Unit,
    onNavigateToProfile: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(),
) {

    val viewModel: HomeViewModel = viewModel(
        key = user.club,
        factory = HomeViewModelFactory(user.club)
    )

    LaunchedEffect(user.club) {
        viewModel.loadModules()
        viewModel.loadName()
    }

    if (viewModel.modules.isEmpty()) return
    if (viewModel.clubName.isEmpty()) return

    var selectedModule by remember { mutableStateOf(ClubModule.RESERVAS) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.clubName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                if (user.role == UserRole.ADMIN) {
                    AdminDrawerSection(
                        onNavigateToUsers = {
                            scope.launch { drawerState.close() }
                            onNavigateToUsers()
                        },
                        onNavigateToConfig = {
                            scope.launch { drawerState.close() }
                            Log.d("HomeScreen", "onNavigateToConfig")
                            onNavigateToConfig()
                        }
                    )
                }

                AccountDrawerSection(
                    onNavigateToProfile = {
                        scope.launch { drawerState.close() }
                        onNavigateToProfile()
                    }
                )

                SupportDrawerSection(
                    onNavigateToAbout = {
                        scope.launch {
                            drawerState.close()
                            showDialog = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(viewModel.clubName) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
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
                    viewModel.modules.forEach { module ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = when (module) {
                                        ClubModule.RESERVAS -> Icons.Default.CalendarMonth
                                        ClubModule.MATCHES -> Icons.Default.SportsTennis
                                        ClubModule.COURSES -> Icons.Default.SportsScore
                                        ClubModule.COMPETITIONS -> Icons.Default.EmojiEvents
                                        ClubModule.RANKING -> Icons.Default.Leaderboard
                                    },
                                    contentDescription = module.name
                                )
                            },
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
                    ClubModule.RESERVAS -> BookingScreen(clubId = user.club, user = user)
                    ClubModule.MATCHES -> MatchesScreen(clubId = user.club, user = user)
                    ClubModule.COURSES -> CoursesScreen(clubId = user.club, user = user)
                    ClubModule.COMPETITIONS -> Text("Módulo no implementado")
                    ClubModule.RANKING -> Text("Módulo no implementado")
                }
            }
        }
    }
    if (showDialog) {
        AboutDialog(onDismiss = { showDialog = false })
    }
}