package com.uniovi.tfg.racketFlex.features.booking.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.booking.presentation.BookingViewModel

@Composable
fun CourtSelector(viewModel: BookingViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.courts.forEach { court ->
            Button(
                onClick = { viewModel.selectCourt(court) },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (viewModel.selectedCourt == court)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary,
                    contentColor =
                        if (viewModel.selectedCourt == court)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(court.id)
            }
        }
    }
}