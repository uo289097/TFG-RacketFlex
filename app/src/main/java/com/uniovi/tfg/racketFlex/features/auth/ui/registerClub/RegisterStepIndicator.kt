package com.uniovi.tfg.racketFlex.features.auth.ui.registerClub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RegisterStepIndicator(currentStep: Int) {
    Spacer(Modifier.height(15.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..3).forEach { step ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (step <= currentStep) MaterialTheme.colorScheme.primary
                        else Color.LightGray,
                        CircleShape
                    )
            )
            if (step < 3) {
                HorizontalDivider(
                    modifier = Modifier.width(40.dp),
                    color = if (step < currentStep) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            }
        }
    }
}