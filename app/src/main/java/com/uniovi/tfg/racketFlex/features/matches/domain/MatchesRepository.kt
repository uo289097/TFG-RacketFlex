package com.uniovi.tfg.racketFlex.features.matches.domain

import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.SetScore
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.TennisMatchType

interface MatchesRepository {
    suspend fun getMatches(clubId: String, sport: Sport): List<Match>
    suspend fun joinMatch(clubId: String, matchId: String, userId: String, index: Int)
    suspend fun getUserMatches(clubId: String, sport: Sport, userId: String): List<Match>
    suspend fun addScore(clubId: String, matchId: String, score: List<SetScore>)
    suspend fun createMatch(
        clubId: String,
        userId: String,
        match: Match
    )
}