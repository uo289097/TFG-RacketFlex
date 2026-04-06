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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.BookingType
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.booking.data.BookingRepositoryImpl

class BookingViewModel(
    private val clubId: String,
    private val repository: BookingRepository = BookingRepositoryImpl(FirebaseFirestore.getInstance())
) : ViewModel() {
    var selectedDay by mutableStateOf(LocalDate.now())
    var selectedSport by mutableStateOf<Sport?>(null)
    var selectedCourt by mutableStateOf<Court?>(null)
    var courts by mutableStateOf<List<Court>>(emptyList())
    var bookings by mutableStateOf<List<Booking>>(emptyList())
    var bookingDuration by mutableIntStateOf(0)
    var openTime by mutableIntStateOf(0)
    var closeTime by mutableIntStateOf(0)

    init {
        getBookingInfo()
    }

    fun getBookingInfo() {
        viewModelScope.launch {
            val bookingInfo = repository.getBookingInfo(clubId)
            bookingDuration = bookingInfo.booking_duration
            openTime = bookingInfo.open_time
            closeTime = bookingInfo.close_time
        }
    }

    fun selectDay(day: LocalDate) {
        selectedDay = day
        selectedSport = null
        selectedCourt = null
        loadBookings()
    }

    fun selectSport(sport: Sport) {
        selectedSport = sport
        selectedCourt = null
        loadCourts()
        loadBookings()
    }

    fun selectCourt(court: Court) {
        selectedCourt = court
        loadBookings()
    }

    private fun loadCourts() {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            courts = repository.getCourts(sport, clubId)
        }
    }

    private fun loadBookings() {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            val timestamp =
                selectedDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            bookings = repository.getBookings(timestamp, sport, clubId)
        }
    }

    // Determina si un slot está reservado
    fun isReserved(court: Court, initTime: Long, endTime: Long): Boolean {
        return bookings.any {
            it.court == court.id &&
                    !(it.endDate <= initTime || it.initDate >= endTime)
        }
    }

    fun createBooking(userId: String, court: Court, initTime: Long, endTime: Long) {
        selectedSport ?: return
        viewModelScope.launch {
            val booking = Booking(
                bookerId = userId,
                court = court.id,
                initDate = initTime,
                endDate = endTime,
                type = BookingType.INDIVIDUAL
            )
            repository.createBooking(clubId, booking)
            loadBookings()
        }

    }

    fun hasReachedDailyLimit(userId: String, userRole: UserRole, limit: Int = 2): Boolean {
        if (userRole == UserRole.ADMIN) return false
        return bookings.count { it.bookerId == userId } >= limit
    }

}

class BookingViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookingViewModel(clubId) as T
    }
}