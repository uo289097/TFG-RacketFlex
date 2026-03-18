package com.uniovi.tfg.racketFlex.core.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String = "",
    val club: String = "",
    val name: String = "",
    val role: UserRole
)

enum class UserRole {
    SOCIO,
    ADMIN
}


