package com.uniovi.tfg.racketFlex.features.auth.ui.registerClub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterClubViewModel

@Composable
fun RegisterClubStep2Screen(
    viewModel: RegisterClubViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val isValid = viewModel.adminEmail.isNotBlank() && viewModel.adminPassword.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RegisterStepIndicator(currentStep = 2)

        //TODO AÑADIR NOMBRE

        OutlinedTextField(
            value = viewModel.adminEmail,
            onValueChange = { viewModel.adminEmail = it },
            label = { Text("Correo admin") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.adminPassword,
            onValueChange = { viewModel.adminPassword = it },
            label = { Text("Contraseña admin") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Atrás")
            }
            Button(
                onClick = onNext,
                enabled = isValid,
                modifier = Modifier.weight(1f)
            ) {
                Text("Siguiente")
            }
        }

        Spacer(Modifier.height(10.dp))
    }
}