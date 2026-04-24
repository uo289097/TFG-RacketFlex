package com.uniovi.tfg.racketFlex.core.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUser(email: String): User? {
        return try {
            val doc = db.collection("users").document(email).get().await()

            if (doc.exists()) {
                Log.d("AppRole", doc.getString("rol").toString())
                User(
                    email = doc.getString("email") ?: "",
                    club = doc.getString("club") ?: "",
                    name = doc.getString("nombre") ?: "",
                    if (doc.getString("rol") == null ||
                        doc.getString("rol") == "socio"
                    ) UserRole.SOCIO else UserRole.ADMIN
                )

            } else null

        } catch (_: Exception) {
            null
        }
    }

    suspend fun getClubModules(clubId: String): List<String> {
        return try {
            val doc = db.collection("clubs")
                .document(clubId)
                .get()
                .await()
            val modules = doc.get("modulos") as? List<*>
            Log.d("modules", "FS ${modules.toString()}")

            modules?.mapNotNull { it as? String } ?: emptyList()
        } catch (e: Exception) {
            Log.d("modules", e.toString())
            emptyList()
        }
    }

}

// TODO SACAR A MAPPER
fun String.toClubModule(): ClubModule? {
    return when (this.lowercase()) {
        "reservas" -> ClubModule.RESERVAS
        "matches" -> ClubModule.MATCHES
        "courses" -> ClubModule.COURSES
        "competitions" -> ClubModule.COMPETITIONS
        "ranking" -> ClubModule.RANKING
        else -> null
    }
}

