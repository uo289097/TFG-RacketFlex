package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    var showScoreDialog by remember { mutableStateOf(false) }

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
                    text = "Creador: ${match.createdBy}",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(4.dp))

                match.players.forEachIndexed { index, player ->
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
                            Button(onClick = { onJoin(index) }) {
                                Text("Apuntarme")
                            }

                        else ->
                            Button(
                                onClick = {},
                                enabled = false,
                                colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Green)
                            ) {
                                Text(
                                    text = player,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                    }
                    if ((index.toDouble() + 1) / match.maxPlayers == 0.5) {
                        HorizontalDivider(color = Color.Gray, modifier = Modifier.width(125.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }



            Column(horizontalAlignment = Alignment.End) {

                Text(dateText, style = MaterialTheme.typography.bodyMedium)

                if (match.score.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.End) {
                        Row {
                            match.score.forEach {
                                Text(
                                    it.player1.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                        }
                        HorizontalDivider(color = Color.Gray, modifier = Modifier.width(60.dp))
                        Row {
                            match.score.forEach {
                                Text(
                                    it.player2.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                        }
                    }
                } else {
                    Text(
                        "${match.players.count { it.isNotBlank() }}/${match.maxPlayers} jugadores",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (match.initDate > System.currentTimeMillis() && match.createdBy == user.email) {
                        Button(onClick = { showScoreDialog = true }) { Text("Añadir resultado") }
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
}

