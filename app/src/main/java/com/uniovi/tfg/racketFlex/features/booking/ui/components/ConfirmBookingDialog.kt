package com.uniovi.tfg.racketFlex.features.booking.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmBookingDialog(
    court: Court,
    initTime: Long,
    endTime: Long,
    selectedDay: LocalDate,
    selectedSport: Sport?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val initLocalTime = Instant.ofEpochMilli(initTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    val endLocalTime = Instant.ofEpochMilli(endTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar reserva") },
        text = {
            Column {
                Text("¿Estás seguro de que quieres reservar la pista con los siguientes datos?")
                Spacer(Modifier.height(8.dp))
                Text("Pista: ${court.id}")
                Text("Deporte: ${selectedSport?.name}")
                Text("Día: $selectedDay")
                Text(
                    "Hora: ${initLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                        endLocalTime.format(
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                    }"
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Aceptar") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}