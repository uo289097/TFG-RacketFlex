package com.uniovi.tfg.racketFlex.features.auth.domain

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean

    suspend fun sendPasswordReset(email: String): Boolean

    suspend fun createUser(email: String, password: String): Boolean
}