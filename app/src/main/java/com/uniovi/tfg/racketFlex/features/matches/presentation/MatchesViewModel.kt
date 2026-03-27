package com.uniovi.tfg.racketFlex.features.matches.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import com.uniovi.tfg.racketFlex.features.matches.data.MatchesRepositoryImpl
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import kotlinx.coroutines.launch

class MatchesViewModel(
    private val clubId: String,
    private val repository: MatchesRepository = MatchesRepositoryImpl(FirebaseFirestore.getInstance())
) : ViewModel() {
    var selectedSport by mutableStateOf(Sport.TENIS)
    var matches by mutableStateOf<List<Match>>(emptyList())

    fun selectSport(sport: Sport) {
        selectedSport = sport
        loadMatches()
    }

    private fun loadMatches() {
        viewModelScope.launch {
            matches = repository.getMatches(clubId, selectedSport)
        }
    }

    fun joinMatch(matchId: String, userId: String, index: Int) {
        viewModelScope.launch {
            repository.joinMatch(clubId, matchId, userId, index)
            loadMatches()
        }
    }

    fun isUserInMatch(match: Match, userId: String) = userId in match.players
    fun isMatchFull(match: Match) = match.players.size >= match.maxPlayers


}

class MatchesViewModelFactory(private val clubId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchesViewModel(clubId) as T
    }
}