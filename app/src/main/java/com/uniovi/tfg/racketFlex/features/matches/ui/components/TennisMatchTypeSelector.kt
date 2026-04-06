package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.TennisMatchType
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel

@Composable
fun TennisMatchTypeSelector(viewModel : MatchesViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TennisMatchType.entries.forEach { mt ->
            Button(
                onClick = { viewModel.selectTennisMatchType(mt) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.selectedTennisMatchType == mt)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                    contentColor = if (viewModel.selectedTennisMatchType == mt)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(mt.name)
            }
        }
    }
}