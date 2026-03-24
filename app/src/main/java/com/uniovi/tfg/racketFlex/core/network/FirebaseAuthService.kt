package com.uniovi.tfg.racketFlex.core.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {

    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun logout() = auth.signOut()

    fun currentUser() = auth.currentUser

    suspend fun sendPasswordReset(email: String): Boolean {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun createUser(email: String, password: String): Boolean {
        return try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            true
        } catch (_: Exception) {
            false
        }
    }

}