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