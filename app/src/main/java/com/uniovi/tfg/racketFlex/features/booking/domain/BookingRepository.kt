package com.uniovi.tfg.racketFlex.features.booking.domain

import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport

interface BookingRepository {
    suspend fun getCourts(sport: Sport, clubId: String): List<Court>
    suspend fun getBookings(dayTimestamp: Long, sport: Sport): List<Booking>
}