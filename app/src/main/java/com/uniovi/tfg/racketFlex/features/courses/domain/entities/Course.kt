package com.uniovi.tfg.racketFlex.features.courses.domain.entities

import com.uniovi.tfg.racketFlex.core.model.Sport
import java.time.DayOfWeek
import java.time.LocalTime

data class Course(
    val id: String,
    val description: String,
    val daysOfWeek: List<DayOfWeek>,
    val sport: Sport,
    val initDate: Long,     // Timestamp
    val endDate: Long,       // Timestamp
    val maxPlayers: Int,
    val players: List<String>,
    val price: Double,
    val startTime: LocalTime?,
    val endTime: LocalTime?
)