package com.uniovi.tfg.racketFlex.features.home

import androidx.lifecycle.ViewModel
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService

class HomeViewModel(
    private val authService: FirebaseAuthService = FirebaseAuthService()
) : ViewModel() {
    fun logout() = authService.logout()
}