package com.uniovi.tfg.racketFlex.features.matches.domain

import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match

interface MatchesRepository {
    suspend fun getMatches(clubId: String, sport: Sport): List<Match>
    suspend fun joinMatch(clubId: String, matchId: String, userId: String, index: Int)
    suspend fun getUserMatches(clubId: String, sport: Sport, userId: String): List<Match>
}