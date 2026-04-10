package com.uniovi.tfg.racketFlex.features.matches.domain.entities

import com.uniovi.tfg.racketFlex.core.model.Sport

data class Match(
    val id: String,
    val bookingId: String,
    val createdBy: String,
    val initDate: Long,
    val maxPlayers: Int,
    val players: List<String>,
    val sport: Sport,
    val score: List<SetScore>,
    var playersNames: List<String>
)


data class SetScore(
    val player1: Int,
    val player2: Int
)

enum class TennisMatchType {
    INDIVIDUAL, DOBLES
}