package com.uniovi.tfg.racketFlex.features.home.ui.subscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.home.presentation.AddUserViewModel
import com.uniovi.tfg.racketFlex.features.home.presentation.AddUserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    clubId: String,
    onBack: () -> Unit
) {

    val viewModel: AddUserViewModel = viewModel(
        key = clubId,
        factory = AddUserViewModelFactory(clubId)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir usuario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = viewModel.repeatedPassword,
                onValueChange = { viewModel.repeatedPassword = it },
                label = { Text("Repetir contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // TODO AÑADIR ROL
            Text(
                "Rol del usuario",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                UserRole.entries.forEach {
                    Button(
                        onClick = { viewModel.selectRole(it) },
                        modifier = Modifier.weight(1f),
                        // TODO CAMBIAR COLORES PARA SELECTED ...
                    ) {
                        Text(it.name)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

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