package com.uniovi.tfg.racketFlex.features.home.domain

import com.uniovi.tfg.racketFlex.core.model.ClubModule

data class ClubInfo(
    val name: String,
    val bookingDuration: Int,
    val bookingPrice: Double,
    val maxBookingsPerDay: Int,
    val modules: List<ClubModule>,
    val numberPadel: Int,
    val numberTenis: Int,
    val openTime: Int,
    val closeTime: Int
)

data class ClubInfoBack(
    val nombre: String,
    val booking_duration: Int,
    val booking_price: Double,
    val max_bookings_per_day: Int,
    val modulos: List<String>,
    val number_padel: Int,
    val number_tennis: Int,
    val open_time: Int,
    val close_time: Int
)
