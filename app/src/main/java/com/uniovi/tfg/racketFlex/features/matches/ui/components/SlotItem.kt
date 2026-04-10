package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun SlotItem(
    user: User,
    slot: LocalTime,
    duration: Duration,
    viewModel: MatchesViewModel,
    enabled: Boolean,
    slots: MutableList<LocalTime>
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    val initTime = viewModel.selectedDay!!.atTime(slot)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    val endTime = initTime + duration.toMillis()
    val end = slot.plus(duration)


    Button(
        onClick = {
            showConfirmDialog = true
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
        ),
        enabled = enabled
    ) {
        Text(
            text = "$slot - $end"
        )
    }

    if (showConfirmDialog) {
        ConfirmMatchDialog(
            initDate = initTime,
            selectedSport = viewModel.selectedSport!!,
            selectedDay = viewModel.selectedDay!!,
            onConfirm = {
                viewModel.createMatch(user, initTime, endTime, slots)
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }
}