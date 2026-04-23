package com.uniovi.tfg.racketFlex.features.home.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.home.domain.ProfileRepository
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val db: FirebaseFirestore
) : ProfileRepository {

    override suspend fun updateName(email: String, name: String) {
        db.collection("users")
            .document(email)
            .update("nombre", name)
            .await()
    }

    override suspend fun getUser(email: String): User {
        val doc = db.collection("users")
            .document(email)
            .get()
            .await()

        return User(
            email = doc.id,
            name = doc.getString("nombre") ?: "",
            club = doc.getString("club") ?: "",
            role = (UserRole.valueOf(doc.getString("rol")?.uppercase() ?: "socio"))
        )
    }

}