package com.uniovi.tfg.racketFlex.features.courses.ui.components

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
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import java.text.NumberFormat
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ConfirmInscriptionDialog(
    course: Course,
    dateText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val priceText = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-ES"))
        .apply { minimumFractionDigits = 2 }
        .format(course.price)
    val daysText = course.daysOfWeek.joinToString(", ") {
        it.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar inscipción") },
        text = {
            Column {
                Text("¿Estás seguro de que quieres inscribirte al siguiente curso?")
                Spacer(Modifier.height(8.dp))
                Text("Título: ${course.title}")
                Text("Fechas: $dateText")
                Text("Días: $daysText")
                Text(
                    "Horas: ${course.startTime} - ${course.endTime}"
                )
                Text("Precio: $priceText €/mes")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirmar") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}