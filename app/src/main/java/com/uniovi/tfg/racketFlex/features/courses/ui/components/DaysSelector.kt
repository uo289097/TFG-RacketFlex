package com.uniovi.tfg.racketFlex.features.courses.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DaysSelector(
    selectedDays: Set<DayOfWeek>,
    onToggle: (DayOfWeek) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        DayOfWeek.entries.forEach { day ->
            FilterChip(
                selected = selectedDays.contains(day),
                onClick = { onToggle(day) },
                label = {
                    Text(
                        day
                            .getDisplayName(
                                TextStyle.SHORT,
                                Locale.forLanguageTag("es")
                            ).uppercase()
                    )
                }
            )
        }
    }
}