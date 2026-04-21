package com.uniovi.tfg.racketFlex.features.courses.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModel
import com.uniovi.tfg.racketFlex.features.courses.ui.components.DaysSelector
import com.uniovi.tfg.racketFlex.features.courses.ui.components.SportDropdown
import com.uniovi.tfg.racketFlex.features.courses.ui.components.TimeSelector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateCourseTab(clubId: String, user: User, viewModel: CoursesViewModel) {
    var datePickerKey by remember { mutableIntStateOf(0) }
    val datePickerState = key(datePickerKey) { rememberDateRangePickerState() }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(
        datePickerState.selectedStartDateMillis,
        datePickerState.selectedEndDateMillis
    ) {
        viewModel.startDate = datePickerState.selectedStartDateMillis
        viewModel.endDate = datePickerState.selectedEndDateMillis
    }

    LaunchedEffect(viewModel.startDate, viewModel.endDate) {
        if (viewModel.startDate == null && viewModel.endDate == null) {
            datePickerKey++
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SportDropdown(
                selected = viewModel.sport,
                onSelected = { viewModel.sport = it }
            )
        }

        item {
            OutlinedTextField(
                value = viewModel.maxPlayers,
                onValueChange = { viewModel.maxPlayers = it },
                label = { Text("Máx jugadores") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        item {
            OutlinedTextField(
                value = viewModel.price,
                onValueChange = { viewModel.price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        item {
            Text("Días de la semana:")
        }

        item {
            DaysSelector(
                selectedDays = viewModel.selectedDays,
                onToggle = viewModel::toggleDay
            )
        }

        item {
            val dateText = buildString {
                val start = viewModel.startDate
                val end = viewModel.endDate
                if (start != null && end != null) {
                    val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    append("Fechas: ${fmt.format(Date(start))} — ${fmt.format(Date(end))}")
                } else {
                    append("Seleccionar fechas")
                }
            }

            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(dateText)
            }
        }

        item {
            TimeSelector(
                label = "⏰ Hora inicio",
                time = viewModel.startTime,
                onTimeSelected = { viewModel.startTime = it }
            )
        }

        item {
            TimeSelector(
                label = "⏰ Hora fin",
                time = viewModel.endTime,
                onTimeSelected = { viewModel.endTime = it }
            )
        }

        item {
            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Button(
                onClick = { viewModel.createCourse() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear curso")
            }
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.startDate = datePickerState.selectedStartDateMillis
                    viewModel.endDate = datePickerState.selectedEndDateMillis
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DateRangePicker(state = datePickerState)
        }
    }
}
