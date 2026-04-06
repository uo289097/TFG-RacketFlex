package com.uniovi.tfg.racketFlex.features.auth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.features.auth.domain.RegisterClubRepository
import kotlinx.coroutines.tasks.await

class RegisterClubRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : RegisterClubRepository {
    override suspend fun clubExists(clubName: String): Boolean {
        return db.collection("clubs").document(clubName).get().await().exists()
    }

    override suspend fun createClub(
        clubName: String,
        modules: List<String>,
        slotDuration: Int,
        numberTennis: Int,
        numberPadel: Int
    ) {
        db.collection("clubs").document(clubName).set(
            hashMapOf(
                "nombre" to clubName,
                "modulos" to modules,
                "booking_duration" to slotDuration,
                "number_tennis" to numberTennis,
                "number_padel" to numberPadel
            )
        ).await()
    }

    override suspend fun createUser(email: String, club: String, name: String) {
        db.collection("users").document(email).set(
            hashMapOf(
                "email" to email,
                "club" to club,
                "rol" to "admin",
                "name" to name
            )
        ).await()
    }
}