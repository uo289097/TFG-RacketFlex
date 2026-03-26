package com.uniovi.tfg.racketFlex.features.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModelFactory
import com.uniovi.tfg.racketFlex.features.booking.ui.components.BookingSlots
import com.uniovi.tfg.racketFlex.features.booking.ui.components.ConfirmBookingDialog
import com.uniovi.tfg.racketFlex.features.booking.ui.components.CourtSelector
import com.uniovi.tfg.racketFlex.features.booking.ui.components.DailyLimitDialog
import com.uniovi.tfg.racketFlex.features.booking.ui.components.DaySelector
import com.uniovi.tfg.racketFlex.features.booking.ui.components.SportSelector
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BookingScreen(
    clubId: String, user: User,
) {
    val viewModel: BookingViewModel = viewModel(
        key = clubId,
        factory = BookingViewModelFactory(clubId)
    )
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
        DaySelector(days, viewModel)

        Spacer(Modifier.height(16.dp))

        // --- Selector de deporte ---
        SportSelector(viewModel)

        Spacer(Modifier.height(16.dp))

        // --- Selector de pista ---
        if (viewModel.selectedSport != null) {
            CourtSelector(viewModel)
        }

        Spacer(Modifier.height(16.dp))


        // --- Slots de reservas ---
        if (viewModel.selectedCourt != null) {
            BookingSlots(
                viewModel,
                slots,
                user,
                slotDuration,
                onShowLimitDialog = { showLimitDialog = true },
                onPendingBooking = { pendingBooking = it })
        }
        pendingBooking?.let { (court, initTime, endTime) ->
            ConfirmBookingDialog(
                court = court,
                initTime = initTime,
                endTime = endTime,
                selectedDay = viewModel.selectedDay,
                selectedSport = viewModel.selectedSport,
                onConfirm = {
                    viewModel.createBooking(user.email, court, initTime, endTime)
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
