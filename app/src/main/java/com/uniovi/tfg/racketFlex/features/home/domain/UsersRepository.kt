package com.uniovi.tfg.racketFlex.features.home.domain

import com.uniovi.tfg.racketFlex.core.model.User

interface UsersRepository {
    suspend fun getUsers(clubId: String): List<User>
}