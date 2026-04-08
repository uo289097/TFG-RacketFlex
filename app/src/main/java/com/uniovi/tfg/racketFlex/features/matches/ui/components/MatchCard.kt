package com.uniovi.tfg.racketFlex.features.matches.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.SetScore
import com.uniovi.tfg.racketFlex.features.matches.ui.components.matchInfo.MatchInfoNotPlayed
import com.uniovi.tfg.racketFlex.features.matches.ui.components.matchInfo.MatchInfoPlayed
import com.uniovi.tfg.racketFlex.features.matches.ui.components.matchInfo.MatchInfoWithScore
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MatchCard(
    match: Match,
    user: User,
    onJoin: (Int) -> Unit,
    onAddScore: (List<SetScore>) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val dateText = Instant.ofEpochMilli(match.initDate)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    val alreadyJoined = user.email in match.players

    val half = match.maxPlayers / 2
    val topPlayers = match.players.take(half)
    val bottomPlayers = match.players.drop(half)
    var showScoreDialog by remember { mutableStateOf(false) }

    Log.d("player_names", match.playersNames.toString())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Creador: ${match.createdBy}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    topPlayers.forEachIndexed { index, player ->
                        PlayerButton(
                            player,
                            match.playersNames[index],
                            user,
                            alreadyJoined
                        ) { onJoin(index) }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    bottomPlayers.forEachIndexed { index, player ->
                        PlayerButton(
                            player,
                            match.playersNames[index + half],
                            user,
                            alreadyJoined
                        ) { onJoin(index + half) }
                    }
                }

                if (match.score.isNotEmpty())
                    MatchInfoWithScore(match, Modifier.weight(1f))
                else if (match.initDate < System.currentTimeMillis()
                    && match.createdBy == user.email
                )
                    MatchInfoPlayed(Modifier.weight(1f)) { showScoreDialog = true }
                else
                    MatchInfoNotPlayed(match, dateText, Modifier.weight(1f))
            }
            if (match.score.isNotEmpty() || match.initDate < System.currentTimeMillis()
                && match.createdBy == user.email
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Fecha: $dateText", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        if (showScoreDialog) {
            AddScoreDialog(
                onDismiss = { showScoreDialog = false },
                onConfirm = { score ->
                    onAddScore(score)
                    showScoreDialog = false
                }
            )
        }
    }
}

@Composable
fun PlayerButton(
    player: String,
    playerName: String,
    user: User,
    alreadyJoined: Boolean,
    onJoin: () -> Unit
) {
    when {
        player == user.email ->
            Button(
                enabled = false,
                onClick = {},
                colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Green)
            ) {
                Text("Apuntado")
            }

        alreadyJoined && player.isBlank() ->
            Button(enabled = false, onClick = {}) {
                Text("Disponible")
            }

        player.isBlank() ->
            Button(onClick = onJoin) {
                Text("Apuntarme")
            }

        else ->
            Button(
                onClick = {},
                enabled = false,
                colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Yellow)
            ) {
                Text(playerName, style = MaterialTheme.typography.bodySmall)
            }
    }
}
