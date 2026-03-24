package com.uniovi.tfg.racketFlex.features.auth.domain

interface RegisterClubRepository {

    suspend fun clubExists(clubName: String): Boolean
    suspend fun createClub(
        clubName: String,
        modules: List<String>,
        slotDuration: Int,
        numberTennis: Int,
        numberPadel: Int
    )

    suspend fun createUser(email: String, club: String)

}