package com.uniovi.tfg.racketFlex.core.model

data class Booking(
    val bookerId: String,
    val court: String,
    val initDate: Long,     // Timestamp
    val endDate: Long,       // Timestamp
    val type: BookingType
)

enum class BookingType {
    INDIVIDUAL,
    MATCH,
    TOURNAMENT,
    COURSE
}

data class BookingInfo(
    val bookingDuration: Int,
    val openTime: Int,
    val closeTime: Int,
    val numberTennis: Int,
    val numberPadel: Int,
    val maxBookingsPerDay: Int
)