package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Sport

@Composable
fun SportSelectorMatches() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Sport.entries.forEach { sport ->
            Button(
                onClick = { /* TODO*/ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    /*if (viewModel.selectedSport == sport)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,*/
                    contentColor = Color.Red
                    /*if (viewModel.selectedSport == sport)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSecondary*/
                )
            ) {
                Text(sport.name)
            }
        }
    }
}