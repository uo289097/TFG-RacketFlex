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
import androidx.compose.ui.text.style.TextAlign
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
            } // TODO Fecha después???

            Spacer(Modifier.height(4.dp))

            if (match.score.isNotEmpty())
                MatchInfoWithScore(match, user, onJoin, dateText)
            // Si empty: Dos opciones, el partido no se ha jugado aún fecha > actual o no se ha añadido score
            else if (match.initDate > System.currentTimeMillis())
                MatchInfoNotPlayed(match, user, onJoin, dateText)
            else
                Text("No añadido resultado")
        }
    }
}

@Composable
fun MatchInfoNotPlayed(
    match: Match,
    user: User,
    onJoin: (Int) -> Unit,
    dateText: String
) {
    val alreadyJoined = user.email in match.players

    val half = match.maxPlayers / 2
    val topPlayers = match.players.take(half)
    val bottomPlayers = match.players.drop(half)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topPlayers.forEachIndexed { index, player ->
                PlayerButton(player, user, alreadyJoined) { onJoin(index) }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            bottomPlayers.forEachIndexed { index, player ->
                PlayerButton(player, user, alreadyJoined) { onJoin(index + half) }
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Fecha y pista")
        }
    }

}

@Composable
fun MatchInfoWithScore(
    match: Match,
    user: User,
    onJoin: (Int) -> Unit,
    dateText: String
) {
    val alreadyJoined = user.email in match.players

    val half = match.maxPlayers / 2
    val topPlayers = match.players.take(half)
    val bottomPlayers = match.players.drop(half)
    val topScores = match.score.map { it.player1 }
    val bottomScores = match.score.map { it.player2 }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topPlayers.forEachIndexed { index, player ->
                PlayerButton(player, user, alreadyJoined) { onJoin(index) }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            bottomPlayers.forEachIndexed { index, player ->
                PlayerButton(player, user, alreadyJoined) { onJoin(index + half) }
            }
        }

        Spacer(Modifier.width(1.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                topScores.forEach { score ->
                    Text(score.toString(), textAlign = TextAlign.Center)
                    Spacer(Modifier.width(10.dp))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Row {
                bottomScores.forEach { score ->
                    Text(score.toString(), textAlign = TextAlign.Center)
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text("Fecha: $dateText", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PlayerButton(
    player: String,
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
                Text(player, style = MaterialTheme.typography.bodySmall)
            }
    }
}

/*Row(
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
            if (match.initDate < System.currentTimeMillis() && match.createdBy == user.email) {
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
}*/
