package com.uniovi.tfg.racketFlex.features.matches.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.ui.components.MatchDaySelector
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SlotItem
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SportSelectorMatches
import com.uniovi.tfg.racketFlex.features.matches.ui.components.TennisMatchTypeSelector
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    val availability by viewModel.slotAvailability.collectAsState()

    LaunchedEffect(viewModel.selectedDay, viewModel.selectedSport) {
        val day = viewModel.selectedDay
        val sport = viewModel.selectedSport

        if (day != null && sport != null) {
            viewModel.checkAvailability(user.email, user.role, slots)
        }
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
                if (!(slot.isBefore(LocalTime.now()) && viewModel.selectedDay == LocalDate.now()))
                    SlotItem(
                        user = user,
                        slot = slot,
                        duration = slotDuration,
                        viewModel = viewModel,
                        enabled = if (viewModel.selectedDay == null || viewModel.selectedSport == null) {
                            false
                        } else {
                            availability[slot] ?: false
                        },
                        slots = slots
                    )
            }
        }
    }
}
