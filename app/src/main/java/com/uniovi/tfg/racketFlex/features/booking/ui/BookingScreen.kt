package com.uniovi.tfg.racketFlex.features.booking.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun BookingScreen(clubId: String, userId: String, viewModel: BookingViewModel = viewModel()) {
    Log.d("club", "$clubId  $userId")
    val days = remember { (0..6).map { LocalDate.now().plusDays(it.toLong()) } }
    val slotDuration = Duration.ofMinutes(90) // 1h30min
    val startHour = 10
    val endHour = 22

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Calendario de días ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Button(
                    onClick = { viewModel.selectDay(day) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedDay == day) Color.Blue else Color.Gray
                    )
                ) {
                    Text(day.dayOfWeek.name.take(3))
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
                    onClick = { viewModel.selectSport(sport, clubId) },
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
                    items = (startHour until endHour).toList(),
                    key = { hour -> "slot_${court.id}_$hour" }
                ) { hour ->

                    val initTime = viewModel.selectedDay.atTime(hour, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    val endTime = initTime + slotDuration.toMillis()

                    val reserved = viewModel.isReserved(court, initTime, endTime)

                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (reserved) Color.Red else Color.Green
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Text("${hour}:00 - ${hour + 1}:00")
                    }
                }

                // Spacer entre pistas
                item(key = "spacer_${court.id}") {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}