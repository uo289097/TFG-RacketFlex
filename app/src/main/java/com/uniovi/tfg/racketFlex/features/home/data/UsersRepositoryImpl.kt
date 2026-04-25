package com.uniovi.tfg.racketFlex.features.home.data

import android.util.Log
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
            val user = User(
                email = doc.id,
                name = doc.getString("nombre") ?: "",
                club = doc.getString("club") ?: "",
                role = UserRole.SOCIO       // TODO
            )
            list.add(user)
        }

        return list
    }
}
