package com.uniovi.tfg.racketFlex.core.repository

interface AuthRepository {

    suspend fun login(email: String, password: String): Boolean;
}