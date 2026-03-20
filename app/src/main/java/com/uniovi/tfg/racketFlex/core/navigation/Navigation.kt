package com.uniovi.tfg.racketFlex.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

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
                LoginScreen { user, modules ->
                    backStack.add(HomeRoute(user, modules))
                }
            }
            entry<HomeRoute> { route ->
                HomeScreen(
                    modules = route.modules,
                    user = route.user
                )
            }

            entry<ReservasRoute> { route ->
                BookingScreen(clubId = route.clubId, userId = route.userId)
            }

            entry<PartidosRoute> {
                MatchesScreen()
            }
        }

    )
}