package com.uniovi.tfg.racketFlex.features.auth.domain

interface AuthRepository {

    suspend fun login(email: String, password: String): Boolean;
}