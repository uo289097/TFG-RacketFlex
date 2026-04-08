package com.uniovi.tfg.racketFlex.features.matches.ui.components

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
import com.uniovi.tfg.racketFlex.core.model.Sport
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmMatchDialog(
    selectedSport: Sport,
    selectedDay: LocalDate,
    initDate: Long,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val initLocalTime = Instant.ofEpochMilli(initDate)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar reserva") },
        text = {
            Column {
                Text("¿Estás seguro de que quieres crear un partido con los siguientes datos?")
                Spacer(Modifier.height(8.dp))
                Text("Deporte: ${selectedSport.name}")
                Text("Día: $selectedDay")
                Text(
                    "Hora: ${initLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
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