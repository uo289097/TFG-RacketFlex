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
