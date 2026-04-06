package com.uniovi.tfg.racketFlex.features.matches.ui.tabs

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.TennisMatchType
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.ui.components.MatchDaySelector
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SportSelectorMatches
import com.uniovi.tfg.racketFlex.features.matches.ui.components.TennisMatchTypeSelector
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CreateMatchTab(
    clubId: String,
    user: User,
    viewModel: MatchesViewModel
) {
    val days = remember { (0..6).map { LocalDate.now().plusDays(it.toLong()) } }
    val slotDuration = remember(viewModel.bookingDuration) {
        Duration.ofMinutes(viewModel.bookingDuration.toLong())
    }
    val startHour = remember(viewModel.openTime) {
        LocalTime.of(
            viewModel.openTime / 60,
            viewModel.openTime % 60
        )
    }

    val endHour = remember(viewModel.closeTime) {
        LocalTime.of(
            viewModel.closeTime / 60,
            viewModel.closeTime % 60
        )

    }
    val slots = remember(slotDuration, startHour, endHour) {
        if (viewModel.bookingDuration == 0) return@remember mutableListOf()
        val result = mutableListOf<LocalTime>()
        var current = startHour
        while (current.plus(slotDuration) <= endHour) {
            result.add(current)
            current = current.plus(slotDuration)
        }
        result
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SportSelectorMatches(viewModel)
        }

        item {
            if (viewModel.selectedSport == Sport.TENIS) {
                TennisMatchTypeSelector(viewModel)
            }
        }

        item {
            if (viewModel.selectedSport == Sport.PADEL ||
                (viewModel.selectedSport == Sport.TENIS && viewModel.selectedTennisMatchType != null)
            ) {
                MatchDaySelector(viewModel, days)
            }
        }

        if (viewModel.selectedDay != null) {
            items(slots) { slot ->
                SlotItem(
                    slot = slot,
                    duration = slotDuration,
                    onClick = { Log.d("booking_info", "Ahora") }
                )
            }
        }
    }
}


@Composable
fun SlotItem(
    slot: LocalTime,
    duration: Duration,
    onClick: () -> Unit
) {
    val end = slot.plus(duration)

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "$slot - $end",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
// TODO ELIMINAR
/*LazyColumn(
modifier = Modifier.fillMaxSize()
) {
    items(
        items = slots,
        key = { slot -> "slot_${slot.hour}_$slot" }
    ) { slot ->
        val initTime = viewModel.selectedDay.atTime(slot)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endTime = initTime + slotDuration.toMillis()

        Button(
            onClick = {
                Log.d("booking_info", "Hola")
                /*if (viewModel.hasReachedDailyLimit(user.email, user.role)) {
                    onShowLimitDialog()
                } else {
                    onPendingBooking(Triple(court, initTime, endTime))
                }
            },
            //enabled = !reserved,
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
}*/