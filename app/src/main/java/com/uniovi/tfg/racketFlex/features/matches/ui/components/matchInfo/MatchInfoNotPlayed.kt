package com.uniovi.tfg.racketFlex.features.matches.ui.components.matchInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match

@Composable
fun MatchInfoNotPlayed(
    match: Match,
    dateText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(dateText, style = MaterialTheme.typography.bodyMedium)
        Text(
            "${match.players.count { it.isNotBlank() }}/${match.maxPlayers} jugadores",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}