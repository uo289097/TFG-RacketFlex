package com.uniovi.tfg.racketFlex.features.auth.ui.registerClub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterClubViewModel
import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterState

@Composable
fun RegisterClubStep3Screen(
    viewModel: RegisterClubViewModel,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RegisterStepIndicator(currentStep = 3)

        Text("Resumen", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Club: ${viewModel.clubName}")
        Text("Módulos: ${viewModel.selectedModules.joinToString { it.name }}")
        Text("Apertura: ${viewModel.openingHour}:00")
        Text("Cierre: ${viewModel.closingHour}:00")
        Text("Pistas tenis: ${viewModel.tennisCourts}")
        Text("Pistas pádel: ${viewModel.padelCourts}")
        Text("Duración reserva: ${viewModel.slotDuration} min")
        Spacer(Modifier.height(8.dp))
        Text("Admin: ${viewModel.adminEmail}")

        if (uiState is RegisterState.Error) {
            Text(
                text = (uiState as RegisterState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (uiState is RegisterState.Loading) {
            CircularProgressIndicator()
        }

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Atrás")
            }
            Button(
                onClick = { viewModel.register() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Confirmar")
            }
        }
        Spacer(Modifier.height(10.dp))
    }

    LaunchedEffect(uiState) {
        if (uiState is RegisterState.Success) {
            onConfirm()
        }
    }
}