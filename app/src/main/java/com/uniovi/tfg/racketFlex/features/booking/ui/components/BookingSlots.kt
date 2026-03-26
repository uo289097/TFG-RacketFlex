package com.uniovi.tfg.racketFlex.features.booking.ui.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BookingSlots(
    viewModel: BookingViewModel,
    slots: MutableList<LocalTime>,
    user: User,
    slotDuration: Duration,
    onShowLimitDialog: () -> Unit,
    onPendingBooking: (Triple<Court, Long, Long>) -> Unit
) {
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
                        if (viewModel.hasReachedDailyLimit(user.email, user.role)) {
                            onShowLimitDialog()
                        } else {
                            onPendingBooking(Triple(court, initTime, endTime))
                        }
                    },
                    enabled = !reserved,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,

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