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
    val booking_duration: Int,
    val open_time: Int,
    val close_time: Int,
    val number_tennis: Int,
    val number_padel: Int
)