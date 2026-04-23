package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.features.home.data.ConfigRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.ClubInfo
import com.uniovi.tfg.racketFlex.features.home.domain.ConfigRepository
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val clubId: String,
    private val configRepository: ConfigRepository = ConfigRepositoryImpl(FirebaseFirestore.getInstance()),
) : ViewModel() {

    var name by mutableStateOf(clubId)
    var numberPadel by mutableStateOf("")
    var numberTenis by mutableStateOf("")
    var bookingDuration by mutableStateOf("")
    var bookingPrice by mutableStateOf("")
    var maxBookingsPerDay by mutableStateOf("")
    var openTime by mutableStateOf("")
    var closeTime by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)

    var clubInfo by mutableStateOf<ClubInfo?>(null)

    init {
        loadClub()
    }

    fun loadClub() {
        viewModelScope.launch {
            clubInfo = configRepository.getClubInfo(clubId)
            val info = clubInfo ?: return@launch
            name = info.name
            numberPadel = info.numberPadel.toString()
            numberTenis = info.numberTenis.toString()
            bookingDuration = info.bookingDuration.toString()
            bookingPrice = info.bookingPrice.toString()
            maxBookingsPerDay = info.maxBookingsPerDay.toString()
            openTime = info.openTime.toString()
            closeTime = info.closeTime.toString()

            errorMessage = null
        }

    }

    fun saveChanges(onSuccess: () -> Unit) {
        /*viewModelScope.launch {
            try {
                errorMessage = null

                val shouldUpdateName = name.trim() != user.name.trim()
                val shouldChangePassword =
                    actualPassword.isNotEmpty() &&
                            newPassword.isNotEmpty() &&
                            repeatedPassword.isNotEmpty()

                if (shouldChangePassword) {
                    if (newPassword != repeatedPassword) {
                        errorMessage = "Las contraseñas no coinciden"
                        return@launch
                    }

                    if (newPassword.length < 6) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        return@launch
                    }

                    if (actualPassword == newPassword) {
                        errorMessage = "La contraseña actual no puede ser la misma que la nueva"
                        return@launch
                    }
                }

                if (shouldChangePassword) {
                    changePassword()
                }

                if (shouldUpdateName) {
                    profileRepository.updateName(user.email, name)
                }
                if (errorMessage == null)
                    onSuccess()

            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al guardar cambios"
                Log.d("ProfileViewModel", "Error al guardar cambios: ${e.message}")
            }
        }*/
    }

}

class ConfigViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigViewModel(clubId) as T
    }
}