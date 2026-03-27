package com.uniovi.tfg.racketFlex.features.matches.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SportSelectorMatches
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SearchMatchesTab(clubId: String, user: User, viewModel: MatchesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Selector deporte
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Sport.entries.forEach { sport ->
                Button(
                    onClick = { viewModel.selectSport(sport) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedSport == sport)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary,
                        contentColor = if (viewModel.selectedSport == sport)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(sport.name)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.matches, key = { it.id }) { match ->
                MatchCard(match = match, user = user, onJoin = { index ->
                    viewModel.joinMatch(match.id, user.email, index)
                })
            }
        }
    }
}

@Composable
private fun MatchCard(match: Match, user: User, onJoin: (Int) -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val dateText = Instant.ofEpochMilli(match.initDate)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    val alreadyJoined = user.email in match.players
    val isFull = match.players.size >= match.maxPlayers

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = match.createdBy,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(4.dp))
                match.players.forEachIndexed { index, player ->
                    when {
                        player == user.email -> {
                            Button(enabled = false, onClick = {}) {
                                Text("Apuntado")
                            }
                        }

                        player.isBlank() -> {
                            Button(onClick = { onJoin(index) }) {
                                Text("Apuntarme")
                            }
                        }

                        else -> {
                            Text(
                                text = player,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(dateText, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${match.players.size}/${match.maxPlayers} jugadores",      // TODO Cambiar
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


/*        ExtendedFloatingActionButton(
            text = { Text("Crear partido") },
            icon = { Icon(Icons.Filled.Add, "Crear partido") },
            onClick = { },
            containerColor = Color.Blue,
            contentColor = Color.Red,
        )*/