package com.uniovi.tfg.racketFlex.core.repository

interface AuthRepository {

    suspend fun login(userId: String, password: String);
}