package com.uniovi.tfg.racketFlex.core.navigation

import androidx.navigation3.runtime.NavKey
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {

    @Serializable
    data object LoginRoute : Routes()

    @Serializable
    data class HomeRoute(
        val user: User,
    ) : Routes()

    @Serializable
    data class ProfileRoute(
        val user: User
    ) : Routes()

    @Serializable
    data class ConfigRoute(
        val clubId: String,
    ) : Routes()

    @Serializable
    data class UsersRoute(
        val clubId: String,
        val user: User
    ) : Routes()

    @Serializable
    data class AddUserRoute(
        val clubId: String,
    ) : Routes()

    @Serializable
    data class BookingsRoute(
        val clubId: String,
        val user: User
    ) : Routes()

    @Serializable
    data class MatchesRoute(
        val clubId: String,
        val user: User
    ) : Routes()

    @Serializable
    data class CoursesRoute(
        val clubId: String,
        val user: User
    ) : Routes()

    @Serializable
    data object ErrorRoute : Routes()


    @Serializable
    data object RegisterClubStep1Route : Routes()

    @Serializable
    data object RegisterClubStep2Route : Routes()

    @Serializable
    data object RegisterClubStep3Route : Routes()
}