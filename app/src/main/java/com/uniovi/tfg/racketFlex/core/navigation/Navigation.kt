package com.uniovi.tfg.racketFlex.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

import com.uniovi.tfg.racketFlex.core.navigation.Routes.LoginRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.BookingRoute
import com.uniovi.tfg.racketFlex.features.auth.ui.LoginScreen
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(LoginRoute)

    NavDisplay(
        backStack = backStack,
        onBack = {},
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen {
                    backStack.add(BookingRoute)
                }
            }
            entry<BookingRoute> {
                BookingScreen()
            }
        }

    )
}