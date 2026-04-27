package com.uniovi.tfg.racketFlex.features.home.domain

import com.uniovi.tfg.racketFlex.core.model.User

interface ProfileRepository {
    suspend fun updateName(email: String, name: String)
    suspend fun getUser(email: String): User
}