package com.uniovi.tfg.racketFlex.features.courses.ui.tabs

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Composable
fun CreateCourseTab(clubId: String, user: User, viewModel: CoursesViewModel) {
    var datePickerKey by remember { mutableStateOf(0) }
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
            // 📝 Título
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // 📝 Descripción
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // 🎾 Selector deporte
            SportDropdown(
                selected = viewModel.sport,
                onSelected = { viewModel.sport = it }
            )
        }

        item {
            // 👥 Max players
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
            // 📅 Días de la semana
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
            // ✅ Crear
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

@Composable
fun SportDropdown(
    selected: Sport?,
    onSelected: (Sport) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Deporte") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, null)
            }
        )

        // Box transparente encima que captura el click
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Sport.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

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