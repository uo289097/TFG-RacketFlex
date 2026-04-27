package com.uniovi.tfg.racketFlex.features.home.domain

interface ConfigRepository {
    suspend fun getClubInfo(clubId: String): ClubInfo
    suspend fun updateClub(clubId: String, club: ClubInfoBack)
}