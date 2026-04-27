package com.uniovi.tfg.racketFlex.features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.network.FirebaseAuthService
import com.uniovi.tfg.racketFlex.core.network.FirestoreService
import com.uniovi.tfg.racketFlex.core.network.toClubModule
import kotlinx.coroutines.launch

class HomeViewModel(
    private val clubId: String,
    private val authService: FirebaseAuthService = FirebaseAuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) : ViewModel() {
    var modules by mutableStateOf<List<ClubModule>>(emptyList())
    var clubName by mutableStateOf("")


    init {
        loadModules()
        loadName()
    }

    fun loadModules() {
        viewModelScope.launch {
            modules = firestoreService.getClubModules(clubId).mapNotNull { it.toClubModule() }
        }
    }

    fun loadName() {
        viewModelScope.launch {
            clubName = firestoreService.getClubName(clubId)
        }
    }

    fun logout() = authService.logout()
}

class HomeViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(clubId) as T
    }
}