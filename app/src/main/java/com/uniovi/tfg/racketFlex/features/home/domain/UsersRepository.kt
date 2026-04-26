package com.uniovi.tfg.racketFlex.features.home.domain

import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole

interface UsersRepository {
    suspend fun getUsers(clubId: String): List<User>
    suspend fun updateRole(userId: String, role: UserRole)
}