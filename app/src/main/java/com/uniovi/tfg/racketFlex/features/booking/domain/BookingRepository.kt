package com.uniovi.tfg.racketFlex.features.booking.domain

import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.BookingInfo
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport

interface BookingRepository {
    suspend fun getCourts(sport: Sport, clubId: String): List<Court>
    suspend fun getBookings(dayTimestamp: Long, sport: Sport, clubId: String): List<Booking>
    suspend fun createBooking(clubId: String, booking: Booking)
    suspend fun createMatchBooking(clubId: String, booking: Booking): String
    suspend fun getBookingInfo(clubId: String): BookingInfo
    suspend fun getFirstAvailableCourt(
        clubId: String,
        sport: Sport,
        initDate: Long,
        endDate: Long
    ): String?
}