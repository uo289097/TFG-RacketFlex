package com.uniovi.tfg.racketFlex.features.booking.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.domain.BookingRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.features.booking.data.BookingRepositoryImpl

class BookingViewModel(
    private val repository: BookingRepository = BookingRepositoryImpl(FirebaseFirestore.getInstance())
) : ViewModel() {
    var selectedDay by mutableStateOf(LocalDate.now())
    var selectedSport by mutableStateOf(Sport.TENIS)
    var selectedCourt by mutableStateOf<Court?>(null)
    var courts by mutableStateOf<List<Court>>(emptyList())
    var bookings by mutableStateOf<List<Booking>>(emptyList())

    init {
    }

    fun selectDay(day: LocalDate) {
        selectedDay = day
        loadBookings()
    }

    fun selectSport(sport: Sport, clubId: String) {
        selectedSport = sport
        selectedCourt = null
        loadCourts(clubId)
        loadBookings()
    }

    fun selectCourt(court: Court) {
        selectedCourt = court
        loadBookings()
    }

    private fun loadCourts(clubId: String) {
        viewModelScope.launch {
            courts = repository.getCourts(selectedSport, clubId)
        }
    }

    private fun loadBookings() {
        viewModelScope.launch {
            val timestamp =
                selectedDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            bookings = repository.getBookings(timestamp, selectedSport)
        }
    }

    // Función para determinar si un slot está reservado
    fun isReserved(court: Court, initTime: Long, endTime: Long): Boolean {
        return bookings.any {
            it.court == court.id &&
                    !(it.endDate <= initTime || it.initDate >= endTime)
        }
    }
}