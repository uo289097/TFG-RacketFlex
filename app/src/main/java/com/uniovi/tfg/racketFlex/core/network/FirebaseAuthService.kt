package com.uniovi.tfg.racketFlex.core.network

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService {

    private val auth = FirebaseAuth.getInstance()

    fun login(username: String, password: String) =
        auth.signInWithEmailAndPassword(username, password)

    fun logout() = auth.signOut()

    fun currentUser() = auth.currentUser

}