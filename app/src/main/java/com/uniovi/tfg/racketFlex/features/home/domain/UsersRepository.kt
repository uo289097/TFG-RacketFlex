package com.uniovi.tfg.racketFlex.features.home.domain

import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole

interface UsersRepository {
    suspend fun getUsers(clubId: String): List<User>
    suspend fun updateRole(userId: String, role: UserRole)
    suspend fun checkUser(email: String, clubId: String): Boolean
    suspend fun createUser(user: User)
    suspend fun updateUserName(email: String, newName: String)
    suspend fun deleteUser(email: String, clubId: String)
}