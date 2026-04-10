package com.uniovi.tfg.racketFlex.features.matches.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.SetScore

@Composable
fun AddScoreDialog(
    onDismiss: () -> Unit,
    onConfirm: (List<SetScore>) -> Unit
) {
    var setsInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    fun parseScore(input: String): List<SetScore>? {
        return try {
            input.trim().split(",").map { set ->
                val parts = set.trim().split("-")
                if (parts.size != 2) return null
                SetScore(parts[0].trim().toInt(), parts[1].trim().toInt())
            }
        } catch (e: Exception) {
            null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir resultado") },
        text = {
            Column {
                Text("Introduce el resultado por sets separados por comas.")
                Text(
                    "Ejemplo: 7-6, 2-6, 6-4",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = setsInput,
                    onValueChange = {
                        setsInput = it
                        error = null
                    },
                    label = { Text("Resultado") },
                    placeholder = { Text("7-6, 2-6, 6-4") },
                    isError = error != null,
                    supportingText = error?.let { { Text(it) } }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val score = parseScore(setsInput)
                if (score.isNullOrEmpty()) {
                    error = "Formato incorrecto"
                } else {
                    onConfirm(score)
                }
            }) { Text("Confirmar") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}