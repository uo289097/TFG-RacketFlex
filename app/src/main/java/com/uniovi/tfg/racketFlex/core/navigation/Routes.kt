package com.uniovi.tfg.racketFlex.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {

    @Serializable
    data object LoginRoute : Routes()

    @Serializable
    data object BookingRoute : Routes()

    @Serializable
    data object ErrorRoute : Routes()

}