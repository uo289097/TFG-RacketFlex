package com.uniovi.tfg.racketFlex.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.core.repository.AuthRepository
import com.uniovi.tfg.racketFlex.features.auth.ui.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthService())
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginState.Error("Campos obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginState.Loading

            val success = authRepository.login(username, password)

            if (success) {
                _uiState.value = LoginState.Success
            } else {
                _uiState.value = LoginState.Error("Credenciales incorrectas")
            }
        }
    }
}