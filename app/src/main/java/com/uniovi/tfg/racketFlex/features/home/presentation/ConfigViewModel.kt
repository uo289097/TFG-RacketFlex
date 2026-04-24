package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.features.home.data.ConfigRepositoryImpl
import com.uniovi.tfg.racketFlex.features.home.domain.ClubInfo
import com.uniovi.tfg.racketFlex.features.home.domain.ClubInfoBack
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
    var openTime by mutableIntStateOf(0)
    var closeTime by mutableIntStateOf(0)
    var modules by mutableStateOf<List<ClubModule>>(emptyList())

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
            modules = info.modules
            numberPadel = info.numberPadel.toString()
            numberTenis = info.numberTenis.toString()
            bookingDuration = info.bookingDuration.toString()
            bookingPrice = info.bookingPrice.toString()
            maxBookingsPerDay = info.maxBookingsPerDay.toString()
            openTime = info.openTime
            closeTime = info.closeTime

            errorMessage = null
        }

    }

    fun toggleModule(module: ClubModule) {
        if (module == ClubModule.RESERVAS) return
        modules = if (modules.contains(module)) {
            modules - module
        } else {
            modules + module
        }
    }

    fun formatTime(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return "%02d:%02d".format(hours, mins)
    }

    fun saveChanges(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                errorMessage = null

                // VALIDACIONES
                if (name.isBlank()) {
                    errorMessage = "El nombre no puede estar vacío"
                    return@launch
                }

                if (modules.isEmpty()) {
                    errorMessage = "Debe haber al menos un módulo activo"
                    return@launch
                }

                val padel = numberPadel.toIntOrNull()
                val tenis = numberTenis.toIntOrNull()
                val duration = bookingDuration.toIntOrNull()
                val price = bookingPrice.toDoubleOrNull()
                val maxBookings = maxBookingsPerDay.toIntOrNull()
                val open = openTime
                val close = closeTime

                if (padel == null || tenis == null) {
                    errorMessage = "Número de pistas inválido"
                    return@launch
                }

                if (duration == null || duration <= 0) {
                    errorMessage = "Duración inválida"
                    return@launch
                }

                if (price == null || price < 0) {
                    errorMessage = "Precio inválido"
                    return@launch
                }

                if (open >= close) {
                    errorMessage = "Horario inválido"
                    return@launch
                }

                val updatedClub = ClubInfoBack(
                    nombre = name,
                    modulos = modules.map { it.name.lowercase() },
                    number_padel = padel,
                    number_tennis = tenis,
                    booking_duration = duration,
                    booking_price = price,
                    max_bookings_per_day = maxBookings ?: 0,
                    open_time = open,
                    close_time = close
                )
                configRepository.updateClub(clubId, updatedClub)

                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al guardar cambios"
            }
        }
    }

}

class ConfigViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigViewModel(clubId) as T
    }
}