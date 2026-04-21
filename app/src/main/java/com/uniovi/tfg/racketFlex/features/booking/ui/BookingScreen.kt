package com.uniovi.tfg.racketFlex.features.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.Court
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
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun BookingScreen(
    clubId: String, user: User,
) {
    val viewModel: BookingViewModel = viewModel(
        key = user.email,
        factory = BookingViewModelFactory(clubId)
    )
    val bookingInfo = viewModel.bookingInfo ?: return
    val days = remember { (0..6).map { LocalDate.now().plusDays(it.toLong()) } }
    val slotDuration = remember(bookingInfo.booking_duration) {
        Duration.ofMinutes(bookingInfo.booking_duration.toLong())
    }
    val startHour = remember(bookingInfo.open_time) {
        LocalTime.of(
            bookingInfo.open_time / 60,
            bookingInfo.open_time % 60
        )
    }

    val endHour = remember(bookingInfo.close_time) {
        LocalTime.of(
            bookingInfo.close_time / 60,
            bookingInfo.close_time % 60
        )

    }
    val slots = remember(slotDuration, startHour, endHour) {
        if (bookingInfo.booking_duration == 0) return@remember mutableListOf()
        val result = mutableListOf<LocalTime>()
        var current = startHour
        while (current.plus(slotDuration) <= endHour) {
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
