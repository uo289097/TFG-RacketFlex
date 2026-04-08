package com.uniovi.tfg.racketFlex.features.matches.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.ui.components.MatchCard
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SportSelectorMatches

@Composable
fun SearchMatchesTab(
    clubId: String,
    user: User,
    viewModel: MatchesViewModel
) {
    LaunchedEffect(viewModel.selectedSport) {
        viewModel.loadMatches()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SportSelectorMatches(viewModel)

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.matches) { match ->
                MatchCard(
                    match = match,
                    user = user,
                    onJoin = { index -> viewModel.joinMatch(match.id, user.email, index) },
                    onAddScore = { score -> viewModel.addScore(match.id, score, user.email) }
                )
            }
        }
    }
}