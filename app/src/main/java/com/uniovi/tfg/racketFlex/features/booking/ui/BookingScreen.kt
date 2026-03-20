package com.uniovi.tfg.racketFlex.features.booking.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModelFactory
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookingScreen(
    clubId: String, userId: String,
) {
    val viewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(clubId)
    )
    Log.d("club", "$clubId  $userId")
    val days = remember { (0..6).map { LocalDate.now().plusDays(it.toLong()) } }
    val slotDuration = Duration.ofMinutes(90) // 1h30min
    val startHour = 10                              // TODO Coger info del club
    val endHour = 22
    val slots = remember(slotDuration, startHour, endHour) {
        val start = LocalTime.of(startHour, 0)
        val end = LocalTime.of(endHour, 0)
        val result = mutableListOf<LocalTime>()
        var current = start
        while (current.plus(slotDuration) <= end) {
            result.add(current)
            current = current.plus(slotDuration)
        }
        result
    }
    var pendingBooking by remember { mutableStateOf<Triple<Court, Long, Long>?>(null) }
    var showLimitDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Calendario de días ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            days.forEach { day ->
                Button(
                    onClick = { viewModel.selectDay(day) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedDay == day) Color.Blue else Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.8f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.dayOfWeek.getDisplayName(
                                TextStyle.SHORT,
                                Locale.forLanguageTag("es")
                            ).uppercase(),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = day.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- Selector de deporte ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Sport.entries.forEach { sport ->
                Button(
                    onClick = { viewModel.selectSport(sport) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedSport == sport) Color.Blue else Color.Gray
                    )
                ) {
                    Text(sport.name)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- Selector de pista ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.courts.forEach { court ->
                Button(
                    onClick = { viewModel.selectCourt(court) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedCourt == court) Color.Green else Color.LightGray
                    )
                ) {
                    Text(court.id)
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        // --- Slots de reservas ---
        if (viewModel.selectedCourt != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val filteredCourts = viewModel.courts.filter {
                    viewModel.selectedCourt == null || it == viewModel.selectedCourt
                }

                filteredCourts.forEach { court ->

                    // Cabecera de cada pista
                    item(key = "header_${court.id}") {      //TODO Key necesario?
                        Text(
                            text = "Pista: ${court.id}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // Slots horarios de esa pista
                    items(
                        items = slots,
                        key = { slot -> "slot_${court.id}_$slot" }
                    ) { slot ->

                        val initTime = viewModel.selectedDay.atTime(slot)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()

                        val endTime = initTime + slotDuration.toMillis()

                        val reserved = viewModel.isReserved(court, initTime, endTime)

                        Button(
                            onClick = {
                                if (viewModel.hasReachedDailyLimit(userId)) {
                                    showLimitDialog = true
                                } else {
                                    pendingBooking = Triple(court, initTime, endTime)
                                }
                            },
                            enabled = !reserved,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            val endLocalTime = slot.plus(slotDuration)
                            Text(
                                "${slot.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                                    endLocalTime.format(
                                        DateTimeFormatter.ofPattern("HH:mm")
                                    )
                                }"
                            )
                        }
                    }

                    // Spacer entre pistas
                    item(key = "spacer_${court.id}") {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        pendingBooking?.let { (court, initTime, endTime) ->
            ConfirmBookingDialog(
                court = court,
                initTime = initTime,
                endTime = endTime,
                selectedDay = viewModel.selectedDay,
                selectedSport = viewModel.selectedSport,
                onConfirm = {
                    viewModel.createBooking(userId, court, initTime, endTime)
                    pendingBooking = null
                },
                onDismiss = { pendingBooking = null }
            )
        }

        if (showLimitDialog) {
            DailyLimitDialog(onDismiss = { showLimitDialog = false })
        }
    }

}

@Composable
private fun ConfirmBookingDialog(
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

@Composable
private fun DailyLimitDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Límite alcanzado") },
        text = { Text("No puedes realizar más de 2 reservas en el mismo día.") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Entendido")
            }
        }
    )
}