package com.uniovi.tfg.racketFlex.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.uniovi.tfg.racketFlex.core.model.ClubModule

import com.uniovi.tfg.racketFlex.core.navigation.Routes.LoginRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.HomeRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.PartidosRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.ReservasRoute

import com.uniovi.tfg.racketFlex.features.auth.ui.LoginScreen
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen
import com.uniovi.tfg.racketFlex.features.home.HomeScreen
import com.uniovi.tfg.racketFlex.features.matches.MatchesScreen

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(LoginRoute)

    NavDisplay(
        backStack = backStack,
        onBack = {},
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen {
                    backStack.add(HomeRoute)
                }
            }
            entry<HomeRoute> {
                HomeScreen(
                    modules = listOf(
                        ClubModule.RESERVAS,
                        ClubModule.PARTIDOS
                    ),
                    onReservasClick = {
                        backStack.add(ReservasRoute)
                    },
                    onPartidosClick = {
                        backStack.add(PartidosRoute)
                    }
                )
            }

            HomeScreen(
                modules = listOf(
                    ClubModule.RESERVAS,
                    ClubModule.PARTIDOS
                ),
                onReservasClick = {
                    backStack.add(ReservasRoute)
                },
                onPartidosClick = {
                    backStack.add(PartidosRoute)
                }
            )

            entry<ReservasRoute> {
                BookingScreen()
            }

            entry<PartidosRoute> {
                MatchesScreen()
            }
        }

    )
}