package com.uniovi.tfg.racketFlex.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginViewModel
import com.uniovi.tfg.racketFlex.features.auth.presentation.LoginState
import com.uniovi.tfg.racketFlex.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navigateToHome: (User, List<ClubModule>) -> Unit
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
            // Icono circular
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
                    .fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
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

                // TODO ???
                is LoginState.Success -> {
                    val data = uiState as LoginState.Success

                    LaunchedEffect(Unit) {
                        navigateToHome(
                            data.user,
                            data.modules
                        )
                    }
                }

                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Olvidaste contraseña
            TextButton(onClick = { viewModel.showResetDialog = true }) {
                Text(text = "¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { /* TODO: acción */ }) {
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
                    enabled = email.isNotBlank()
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
