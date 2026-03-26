package com.uniovi.tfg.racketFlex.features.matches.data

import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository

class MatchesRepositoryImpl(
    private val db: FirebaseFirestore
) : MatchesRepository {
}