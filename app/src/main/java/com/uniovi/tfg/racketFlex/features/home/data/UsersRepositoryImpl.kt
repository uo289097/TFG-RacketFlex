package com.uniovi.tfg.racketFlex.features.home.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.home.domain.UsersRepository
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl(
    private val db: FirebaseFirestore
) : UsersRepository {
    override suspend fun getUsers(clubId: String): List<User> {
        val snapshot = db.collection("users")
            .whereEqualTo("club", clubId)
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

    private fun DocumentSnapshot.toUser(): User {
        return User(
            email = id,
            name = getString("nombre") ?: "",
            club = getString("club") ?: "",
            role = if (getString("rol") == null ||
                getString("rol") == "socio"
            ) UserRole.SOCIO else UserRole.ADMIN
        )
    }
}
