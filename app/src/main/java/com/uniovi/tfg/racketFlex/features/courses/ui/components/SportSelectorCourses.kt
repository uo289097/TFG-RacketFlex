package com.uniovi.tfg.racketFlex.features.courses.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModel

@Composable
fun SportSelectorCourses(viewModel: CoursesViewModel) {
    val bookingInfo = viewModel.bookingInfo ?: return

    val availableSports = buildList {
        if (bookingInfo.numberTennis > 0) add(Sport.TENIS to "Tenis")
        if (bookingInfo.numberPadel > 0) add(Sport.PADEL to "Padel")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableSports.forEach { (sport, label) ->
            val isSelected = viewModel.selectedSport == sport
            Button(
                onClick = { viewModel.selectSport(sport) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSecondary,
                )
            ) {
                Text(label.uppercase())
            }
        }
    }
}