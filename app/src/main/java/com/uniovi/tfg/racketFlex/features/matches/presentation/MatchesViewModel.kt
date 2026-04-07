package com.uniovi.tfg.racketFlex.features.matches.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.BookingType
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.booking.data.BookingRepositoryImpl
import com.uniovi.tfg.racketFlex.features.booking.domain.BookingRepository
import com.uniovi.tfg.racketFlex.features.matches.data.MatchesRepositoryImpl
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.SetScore
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.TennisMatchType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class MatchesViewModel(
    private val clubId: String,
    private val matchesRepository: MatchesRepository = MatchesRepositoryImpl(FirebaseFirestore.getInstance()),
    private val bookingRepository: BookingRepository = BookingRepositoryImpl(FirebaseFirestore.getInstance())
) : ViewModel() {
    var selectedDay by mutableStateOf<LocalDate?>(null)
    var selectedSport by mutableStateOf<Sport?>(null)
    var selectedTennisMatchType by mutableStateOf<TennisMatchType?>(null)
    var matches by mutableStateOf<List<Match>>(emptyList())
    var userMatches by mutableStateOf<List<Match>>(emptyList())
    var bookingDuration by mutableIntStateOf(0)
    var openTime by mutableIntStateOf(0)
    var closeTime by mutableIntStateOf(0)
    var numberTennis by mutableIntStateOf(0)
    var numberPadel by mutableIntStateOf(0)
    val slotAvailability = MutableStateFlow<Map<LocalTime, Boolean>>(emptyMap())

    init {
        getBookingInfo()
    }

    fun getBookingInfo() {
        viewModelScope.launch {
            val bookingInfo = bookingRepository.getBookingInfo(clubId)
            bookingDuration = bookingInfo.booking_duration
            openTime = bookingInfo.open_time
            closeTime = bookingInfo.close_time
            numberTennis = bookingInfo.number_tennis
            numberPadel = bookingInfo.number_padel

        }
    }

    fun selectSport(sport: Sport) {
        selectedSport = sport
        selectedTennisMatchType = null
        selectedDay = null
    }

    fun selectTennisMatchType(matchType: TennisMatchType) {
        selectedTennisMatchType = matchType
    }

    fun selectDay(day: LocalDate) {
        selectedDay = day
    }

    fun loadMatches() {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            matches = matchesRepository.getMatches(clubId, sport)
        }
    }

    fun joinMatch(matchId: String, userId: String, index: Int) {
        viewModelScope.launch {
            matchesRepository.joinMatch(clubId, matchId, userId, index)
            loadMatches()
        }
    }

    fun loadUserMatches(userId: String) {
        val sport = selectedSport ?: return
        viewModelScope.launch {
            userMatches = matchesRepository.getUserMatches(clubId, sport, userId)
        }
    }

    fun createMatch(userId: String, initDate: Long, endDate: Long) {
        val sport = selectedSport ?: return
        if (sport == Sport.TENIS && selectedTennisMatchType == null) return

        viewModelScope.launch {
            val maxPlayers = when {
                sport == Sport.PADEL -> 4
                selectedTennisMatchType == TennisMatchType.INDIVIDUAL -> 2
                else -> 4
            }

            val court = bookingRepository.getFirstAvailableCourt(clubId, sport, initDate, endDate)
                ?: return@launch

            val booking = Booking(
                bookerId = userId,
                court = court,
                initDate = initDate,
                endDate = endDate,
                type = BookingType.MATCH
            )
            val bookingId = bookingRepository.createMatchBooking(clubId, booking)

            val match = Match(
                id = "",
                bookingId = bookingId,
                createdBy = userId,
                initDate = initDate,
                maxPlayers = maxPlayers,
                players = List(maxPlayers) { if (it == 0) userId else "" },
                sport = sport,
                score = emptyList()
            )

            matchesRepository.createMatch(
                clubId,
                userId,
                match,
            )
            loadMatches()
            loadUserMatches(userId)
        }

    }

    fun addScore(matchId: String, score: List<SetScore>, userId: String) {
        viewModelScope.launch {
            matchesRepository.addScore(clubId, matchId, score)
            loadMatches()
            loadUserMatches(userId)
        }
    }

    fun checkAvailability(userId: String, userRole: UserRole, slots: List<LocalTime>) {
        viewModelScope.launch {
            val result = mutableMapOf<LocalTime, Boolean>()

            val day = selectedDay!!.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val bookings = bookingRepository.getBookings(day, selectedSport!!, clubId)

            for (slot in slots) {

                val userLimitReached =
                    bookings.count { it.bookerId == userId } >= 2

                val isFull = when (selectedSport) {
                    Sport.TENIS -> bookings.count {
                        val bookingTime = Instant.ofEpochMilli(it.initDate)
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()

                        bookingTime.hour == slot.hour &&
                                bookingTime.minute == slot.minute &&
                                it.court.startsWith("tenis")
                    } >= numberTennis

                    Sport.PADEL -> bookings.count {
                        val bookingTime = Instant.ofEpochMilli(it.initDate)
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()

                        bookingTime.hour == slot.hour &&
                                bookingTime.minute == slot.minute &&
                                it.court.startsWith("padel")
                    } >= numberPadel

                    else -> false
                }

                result[slot] = !(userLimitReached || isFull)
            }

            slotAvailability.value = result
        }
    }

    fun isUserInMatch(match: Match, userId: String) = userId in match.players
    fun isMatchFull(match: Match) = match.players.size >= match.maxPlayers


}

class MatchesViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchesViewModel(clubId) as T
    }
}