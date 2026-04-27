package com.uniovi.tfg.racketFlex.features.auth.ui.registerClub

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterClubViewModel
import com.uniovi.tfg.racketFlex.features.auth.ui.components.ModuleSelectorRegister
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModel
import com.uniovi.tfg.racketFlex.features.home.ui.components.ModuleSelector
import kotlin.collections.plus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterClubStep1Screen(
    viewModel: RegisterClubViewModel,
    onNext: () -> Unit
) {
    val isValid = viewModel.clubName.isNotBlank()
            && viewModel.openingHour.isNotBlank()
            && viewModel.closingHour.isNotBlank()
            && viewModel.slotDuration.isNotBlank()
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RegisterStepIndicator(currentStep = 1)

        OutlinedTextField(
            value = viewModel.clubName,
            onValueChange = { viewModel.clubName = it },
            label = { Text("Nombre del club") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = if (viewModel.selectedModules.isEmpty()) "Selecciona módulos"
                else viewModel.selectedModules.joinToString { it.name },
                onValueChange = {},
                readOnly = true,
                label = { Text("Módulos") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            // TODO AQUÍ
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                ModuleSelectorRegister(viewModel)
            }
        }

        OutlinedTextField(
            value = viewModel.openingHour,
            onValueChange = { viewModel.openingHour = it },
            label = { Text("Hora apertura (HH:mm)") },
            placeholder = { Text("Ej: 10:00") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.closingHour,
            onValueChange = { viewModel.closingHour = it },
            label = { Text("Hora cierre (HH:mm)") },
            placeholder = { Text("Ej: 10:00") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.tennisCourts,
            onValueChange = { viewModel.tennisCourts = it },
            label = { Text("Nº pistas tenis") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.padelCourts,
            onValueChange = { viewModel.padelCourts = it },
            label = { Text("Nº pistas pádel") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.slotDuration,
            onValueChange = { viewModel.slotDuration = it },
            label = { Text("Duración reserva (minutos)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = isValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }

        Spacer(Modifier.height(10.dp))
    }
}

