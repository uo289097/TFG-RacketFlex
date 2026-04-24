package com.uniovi.tfg.racketFlex.features.auth.presentation

import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(
        val user: User,
    ) : LoginState()

    data class Error(val message: String) : LoginState()
}