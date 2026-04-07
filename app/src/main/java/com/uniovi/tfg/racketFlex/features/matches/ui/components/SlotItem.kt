package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun SlotItem(
    userId: String,
    slot: LocalTime,
    duration: Duration,
    viewModel: MatchesViewModel,
    enabled: Boolean
) {

    val initTime = viewModel.selectedDay!!.atTime(slot)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    val endTime = initTime + duration.toMillis()
    val end = slot.plus(duration)


    Button(
        onClick = {
            viewModel.createMatch(userId, initTime, endTime)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        Text(
            text = "$slot - $end",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}