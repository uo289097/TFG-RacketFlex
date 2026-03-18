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
        val modules: List<ClubModule>
    ) : Routes()

    @Serializable
    data class ReservasRoute(
        val clubId: String,
        val userId: String
    ) : Routes()

    @Serializable
    data object PartidosRoute : Routes()

    @Serializable
    data object ErrorRoute : Routes()

}