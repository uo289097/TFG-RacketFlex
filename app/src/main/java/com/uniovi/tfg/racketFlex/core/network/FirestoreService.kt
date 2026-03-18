package com.uniovi.tfg.racketFlex.core.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import kotlinx.coroutines.tasks.await
import java.io.Console

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUser(email: String): User? {
        return try {
            val doc = db.collection("users").document(email).get().await()

            Log.d("club", doc.exists().toString())
            if (doc.exists()) {
                User(
                    email = doc.getString("email") ?: "",
                    club = doc.getString("club") ?: "",
                    name = doc.getString("nombre") ?: "",
                    if (doc.getString("rol") == null ||
                        doc.getString("rol") == "socio"
                    ) UserRole.SOCIO else UserRole.ADMIN
                )
            } else null

        } catch (e: Exception) {
            Log.d("club", e.toString())
            null
        }
    }

    suspend fun getClubModules(clubId: String): List<String> {
        return try {// TODO
            Log.d("club", clubId)
            val doc = db.collection("clubs").document(clubId).get().await()
            val modules = doc.get("modulos") as? List<*>

            modules?.mapNotNull { it as? String } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

}

// TODO SACAR A MAPPER
fun String.toClubModule(): ClubModule? {
    return when (this.lowercase()) {
        "reservas" -> ClubModule.RESERVAS
        "partidos" -> ClubModule.PARTIDOS
        else -> null
    }
}

