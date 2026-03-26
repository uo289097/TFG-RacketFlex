package com.uniovi.tfg.racketFlex.features.matches.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import com.uniovi.tfg.racketFlex.features.matches.data.MatchesRepositoryImpl
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository

class MatchesViewModel(
    private val clubId: String,
    private val repository: MatchesRepository = MatchesRepositoryImpl(FirebaseFirestore.getInstance())
) : ViewModel() {


}

class MatchesViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchesViewModel(clubId) as T
    }
}