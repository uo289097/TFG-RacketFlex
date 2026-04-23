package com.uniovi.tfg.racketFlex.features.home.ui.subscreens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModel
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(clubId: String, onBack: () -> Unit) {

    val viewModel: ConfigViewModel = viewModel(
        key = clubId,
        factory = ConfigViewModelFactory(clubId)
    )
    val clubInfo = viewModel.clubInfo ?: return
    val context = LocalContext.current

    LaunchedEffect(clubId) {
        viewModel.loadClub()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración del club") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                clubInfo.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(5.dp))
            Text(
                "Cambiar nombre",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Cambiar módulos",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(5.dp))
            // TODO Módulos, sacar de días crear curso
            /*OutlinedTextField(
                value = viewModel.actualPassword,
                onValueChange = { viewModel.actualPassword = it },
                label = { Text("Contraseña actual") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )*/

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Text(
                "Cambiar número de pistas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            OutlinedTextField(
                value = viewModel.numberPadel,
                onValueChange = { viewModel.numberPadel = it },
                label = { Text("Número de pistas de pádel") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.numberTenis,
                onValueChange = { viewModel.numberTenis = it },
                label = { Text("Número de pistas de tenis") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Text(
                "Cambiar información de las reservas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            OutlinedTextField(
                value = viewModel.bookingDuration,
                onValueChange = { viewModel.bookingDuration = it },
                label = { Text("Duración de cada reserva en minutos") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.bookingPrice,
                onValueChange = { viewModel.bookingPrice = it },
                label = { Text("Precio de reserva (en €)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.maxBookingsPerDay,
                onValueChange = { viewModel.maxBookingsPerDay = it },
                label = { Text("Máximo de reservas por día (0 para no límite)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Text(
                "Cambiar información de apertura y cierre",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            OutlinedTextField(
                value = viewModel.openTime,
                onValueChange = { viewModel.openTime = it },
                label = { Text("Hora de apertura") },       // TODO FORMATEAR HORA
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.closeTime,
                onValueChange = { viewModel.closeTime = it },
                label = { Text("Hora de cierre") },          // TODO FORMATEAR HORA
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            viewModel.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onBack() }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    /*viewModel.saveChanges(
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Cambios guardados correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBack()
                        }
                    )*/
                }) {
                    Text("Guardar cambios")
                }
            }

        }
    }
}