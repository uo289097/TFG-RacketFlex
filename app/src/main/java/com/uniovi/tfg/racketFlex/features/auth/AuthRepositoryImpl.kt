package com.uniovi.tfg.racketFlex.features.auth

import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.core.repository.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return firebaseAuthService.login(email, password)
    }
}