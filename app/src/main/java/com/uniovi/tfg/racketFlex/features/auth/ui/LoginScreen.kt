package com.uniovi.tfg.racketFlex.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.R
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginState
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navigateToHome: (User) -> Unit,
    onNavigateToRegister: () -> Unit,
    //navigateToClubSelector: (User) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.racketflex_icon),
                    contentDescription = "RacketFlex Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre app
            Text(
                text = "RacketFlex",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(120.dp))

            // Usuario
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Usuario") },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón login
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Text(text = "Iniciar Sesión")
            }
            when (uiState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoginState.Error -> {
                    Text(
                        text = (uiState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is LoginState.Success -> {
                    val data = uiState as LoginState.Success
                    /*if (data.user.club.size > 1) {
                        LaunchedEffect(uiState) {
                            navigateToClubSelector(data.user)
                        }
                    } else {
                        LaunchedEffect(uiState) {
                            navigateToHome(data.user)
                        }
                    }*/

                    LaunchedEffect(uiState) {
                        navigateToHome(data.user)
                    }
                }

                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Olvidaste contraseña
            TextButton(
                onClick = { viewModel.showResetDialog = true },
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { onNavigateToRegister() },

                ) {
                Text(
                    text = "¿Quieres registrar tu club?",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (viewModel.showResetDialog) {
        ForgotPasswordDialog(
            onDismiss = {
                viewModel.showResetDialog = false
                viewModel.resetEmailSent = false
            },
            onSend = { email -> viewModel.sendPasswordReset(email) },
            emailSent = viewModel.resetEmailSent
        )
    }
}

@Composable
private fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit,
    emailSent: Boolean
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Recuperar contraseña") },
        text = {
            if (emailSent) {
                Text("Se ha enviado un correo de recuperación a $email.")
            } else {
                Column {
                    Text("Introduce tu email y te enviaremos un enlace para restablecer tu contraseña.")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
            }
        },
        confirmButton = {
            if (emailSent) {
                Button(onClick = onDismiss) { Text("Cerrar") }
            } else {
                Button(
                    onClick = { onSend(email) },
                    enabled = email.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
                    )
                ) { Text("Enviar") }
            }
        },
        dismissButton = {
            if (!emailSent) {
                OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}
