package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MatchDaySelector(viewModel: MatchesViewModel, days: List<LocalDate>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        days.forEach { day ->
            Button(
                onClick = { viewModel.selectDay(day) },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (viewModel.selectedDay == day)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface,
                    contentColor =
                        if (viewModel.selectedDay == day)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.8f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.forLanguageTag("es")
                        ).uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = day.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}