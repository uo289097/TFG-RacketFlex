package com.uniovi.tfg.racketFlex.features.home.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl(
    private val db: FirebaseFirestore
) : UsersRepository {
    override suspend fun getUsers(clubId: String): List<User> {
        val snapshot = db.collection("users")
            .whereArrayContains("club", clubId)
            .get()
            .await()

        val list = mutableListOf<User>()
        snapshot.forEach { doc ->
            list.add(doc.toUser())
        }

        return list
    }

    override suspend fun updateRole(userId: String, role: UserRole) {
        db.collection("users")
            .document(userId)
            .update("rol", role.name.lowercase())
            .await()
    }

    override suspend fun checkUser(email: String, clubId: String): Boolean {
        val doc = db.collection("users")
            .document(email)
            .get()
            .await()

        if (!doc.exists())
            return false

        val clubs = (doc.get("club") as? List<String>) ?: return false

        return clubs.contains(clubId)
    }

    override suspend fun createUser(
        user: User
    ) {
        val doc = db.collection("users")
            .document(user.email)
            .get()
            .await()

        if (doc.exists()) {
            db.collection("users")
                .document(user.email)
                .update("club", FieldValue.arrayUnion(user.club))
                .await()
        } else {
            db.collection("users")
                .document(user.email)
                .set(user.toHashMap())
                .await()
        }
    }

    override suspend fun updateUserName(email: String, newName: String) {
        db.collection("users")
            .document(email)
            .update("nombre", newName)
            .await()

    }

    override suspend fun deleteUser(email: String, clubId: String) {
        db.collection("users")
            .document(email)
            .update("club", FieldValue.arrayRemove(clubId))
            .await()
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "email" to email,
            "club" to club,
            "rol" to role.name.lowercase(),
            "nombre" to name
        )
    }


    private fun DocumentSnapshot.toUser(): User {
        return User(
            email = id,
            name = getString("nombre") ?: "",
            club = (get("club") as? List<String>) ?: emptyList(),
            role = if (getString("rol") == null ||
                getString("rol") == "socio"
            ) UserRole.SOCIO else UserRole.ADMIN
        )
    }
}
