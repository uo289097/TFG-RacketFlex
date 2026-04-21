package com.uniovi.tfg.racketFlex.features.courses.ui.components

import android.app.TimePickerDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimeSelector(
    label: String,
    time: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current

    Button(onClick = {
        val now = LocalTime.now()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                onTimeSelected(LocalTime.of(hour, minute))
            },
            now.hour,
            now.minute,
            true
        ).show()
    }) {
        Text("$label: ${time ?: "--:--"}")
    }
}