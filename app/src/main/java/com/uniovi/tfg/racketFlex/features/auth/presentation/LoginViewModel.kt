package com.uniovi.tfg.racketFlex.features.auth.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.core.network.FirestoreService
import com.uniovi.tfg.racketFlex.core.network.toClubModule
import com.uniovi.tfg.racketFlex.features.auth.data.AuthRepositoryImpl
import com.uniovi.tfg.racketFlex.features.auth.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthService()),
    private val firestore: FirestoreService = FirestoreService()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState
    var showResetDialog by mutableStateOf(false)
    var resetEmailSent by mutableStateOf(false)


    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginState.Error("Campos obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginState.Loading

            val success = authRepository.login(username, password)

            if (success) {
                Log.d("club", username)
                val user = firestore.getUser(username)
                Log.d("club", user.toString())
                val modulesRaw = firestore.getClubModules(user!!.club)      // TODO GESTIONAR NULL
                val modules = modulesRaw.mapNotNull { it.toClubModule() }
                _uiState.value = LoginState.Success(
                    user = user,
                    modules = modules
                )

            } else {
                _uiState.value = LoginState.Error("Credenciales incorrectas")
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            val success = authRepository.sendPasswordReset(email)
            resetEmailSent = success
        }
    }

    fun resetState() {
        _uiState.value = LoginState.Idle
    }
}