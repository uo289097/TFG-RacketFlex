package com.uniovi.tfg.racketFlex.features.auth

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun login(username: String, password: String) {
        println("Llegué $username - $password")
    }
}