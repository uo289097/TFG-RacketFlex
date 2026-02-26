package com.uniovi.tfg.racketFlex.features.auth.data

import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.domain.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService
) : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return firebaseAuthService.login(email, password)
    }
}