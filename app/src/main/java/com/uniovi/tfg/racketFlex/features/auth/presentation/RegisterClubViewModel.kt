package com.uniovi.tfg.racketFlex.features.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.features.auth.data.AuthRepositoryImpl
import com.uniovi.tfg.racketFlex.features.auth.data.RegisterClubRepositoryImpl
import com.uniovi.tfg.racketFlex.features.auth.domain.AuthRepository
import com.uniovi.tfg.racketFlex.features.auth.domain.RegisterClubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterClubViewModel(
    private val registerRepository: RegisterClubRepository = RegisterClubRepositoryImpl(),
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthService())
) : ViewModel() {

    // Step 1
    var clubName by mutableStateOf("")
    var selectedModules by mutableStateOf<Set<ClubModule>>(emptySet())
    var openingHour by mutableStateOf("")
    var closingHour by mutableStateOf("")
    var tennisCourts by mutableStateOf("")
    var padelCourts by mutableStateOf("")
    var slotDuration by mutableStateOf("")

    // Step 2
    var adminName by mutableStateOf("")
    var adminEmail by mutableStateOf("")
    var adminPassword by mutableStateOf("")

    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val uiState: StateFlow<RegisterState> = _uiState


    fun register() {
        viewModelScope.launch {
            val openMinutes = parseHourToMinutes(openingHour)
            val closeMinutes = parseHourToMinutes(closingHour)

            if (openMinutes == null || closeMinutes == null) {
                _uiState.value = RegisterState.Error("Formato de hora inválido (HH:mm)")
                return@launch
            }
            _uiState.value = RegisterState.Loading
            try {
                if (registerRepository.clubExists(clubName)) {
                    _uiState.value = RegisterState.Error("Ya existe un club con ese nombre")
                    return@launch
                }

                authRepository.createUser(adminEmail, adminPassword)

                registerRepository.createClub(
                    clubName = clubName,
                    modules = selectedModules.map { it.name.lowercase() },
                    slotDuration = slotDuration.toIntOrNull() ?: 0,
                    numberTennis = tennisCourts.toIntOrNull() ?: 0,
                    numberPadel = padelCourts.toIntOrNull() ?: 0,
                    openTime = openMinutes,
                    closeTime = closeMinutes
                )

                registerRepository.createUser(adminEmail, clubName, adminName)

                _uiState.value = RegisterState.Success

            } catch (e: Exception) {
                _uiState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun parseHourToMinutes(time: String): Int? {
        return try {
            val parts = time.split(":")
            if (parts.size != 2) return null

            val hour = parts[0].toInt()
            val minute = parts[1].toInt()

            if (hour !in 0..23 || minute !in 0..59) return null

            hour * 60 + minute
        } catch (e: Exception) {
            null
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}