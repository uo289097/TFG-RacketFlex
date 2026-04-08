package com.uniovi.tfg.racketFlex.features.matches.ui.components.matchInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match

@Composable
fun MatchInfoWithScore(
    match: Match,
    modifier: Modifier = Modifier
) {
    val topScores = match.score.map { it.player1 }
    val bottomScores = match.score.map { it.player2 }

    Column(
        modifier = modifier,
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