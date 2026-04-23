package com.uniovi.tfg.racketFlex.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

import com.uniovi.tfg.racketFlex.core.navigation.Routes.LoginRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.HomeRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.MatchesRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.BookingsRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.CoursesRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.ConfigRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.ProfileRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.UsersRoute
import com.uniovi.tfg.racketFlex.core.navigation.Routes.RegisterClubStep1Route
import com.uniovi.tfg.racketFlex.core.navigation.Routes.RegisterClubStep2Route
import com.uniovi.tfg.racketFlex.core.navigation.Routes.RegisterClubStep3Route

import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterClubViewModel
import com.uniovi.tfg.racketFlex.features.auth.ui.LoginScreen
import com.uniovi.tfg.racketFlex.features.auth.ui.registerClub.RegisterClubStep1Screen
import com.uniovi.tfg.racketFlex.features.auth.ui.registerClub.RegisterClubStep2Screen
import com.uniovi.tfg.racketFlex.features.auth.ui.registerClub.RegisterClubStep3Screen
import com.uniovi.tfg.racketFlex.features.booking.ui.BookingScreen
import com.uniovi.tfg.racketFlex.features.courses.ui.CoursesScreen
import com.uniovi.tfg.racketFlex.features.home.ui.HomeScreen
import com.uniovi.tfg.racketFlex.features.home.ui.subscreens.ProfileScreen
import com.uniovi.tfg.racketFlex.features.matches.ui.MatchesScreen

@Composable
fun Navigation() {
    val backStack = rememberNavBackStack(LoginRoute)
    val registerViewModel: RegisterClubViewModel = viewModel()  // fuera del entryProvider

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen(
                    navigateToHome = { user, modules ->
                        backStack.add(HomeRoute(user, modules))
                    },
                    onNavigateToRegister = {
                        backStack.add(RegisterClubStep1Route)
                    }
                )
            }
            entry<HomeRoute> { route ->
                HomeScreen(
                    modules = route.modules,
                    user = route.user,
                    onLogout = {
                        backStack.removeIf { it !is LoginRoute }
                    },
                    onNavigateToUsers = {
                        backStack.add(UsersRoute(route.user.club, route.user))
                    },
                    onNavigateToConfig = {
                        backStack.add(ConfigRoute(route.user.club, route.user))
                    },
                    onNavigateToProfile = {
                        backStack.add(ProfileRoute(route.user))
                    }
                )
            }

            entry<BookingsRoute> { route ->
                BookingScreen(clubId = route.clubId, user = route.user)
            }

            entry<MatchesRoute> { route ->
                MatchesScreen(clubId = route.clubId, user = route.user)
            }

            entry<CoursesRoute> { route ->
                CoursesScreen(clubId = route.clubId, user = route.user)
            }

            // TODO AÑADIR RUTAS MENU LATERAL
            entry<ProfileRoute> { route ->
                ProfileScreen(
                    user = route.user,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<RegisterClubStep1Route> {
                RegisterClubStep1Screen(
                    viewModel = registerViewModel,
                    onNext = { backStack.add(RegisterClubStep2Route) }
                )
            }

            entry<RegisterClubStep2Route> {
                RegisterClubStep2Screen(
                    viewModel = registerViewModel,
                    onNext = { backStack.add(RegisterClubStep3Route) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<RegisterClubStep3Route> {
                RegisterClubStep3Screen(
                    viewModel = registerViewModel,
                    onConfirm = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }

    )
}