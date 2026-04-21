package com.uniovi.tfg.racketFlex.features.home.ui.components

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val email = "uo289097@uniovi.es"
    val githubUrl = "https://github.com/TODO"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        title = {
            Text("ℹ️ Acerca de")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Racket Flex",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Hecho por: Gabriel García Martínez")
                Text("TFG - Ingeniería Informática del Software")
                Text("2026")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📧 $email")

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            scope.launch {
                                val clipData = ClipData.newPlainText(email, email)
                                clipboardManager.setClipEntry(clipData.toClipEntry())
                            }
                            Toast.makeText(
                                context,
                                "Email copiado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar email"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // TODO
                /*Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, githubUrl.toUri())
                        context.startActivity(intent)
                    }
                ) {
                    Text("Ver GitHub")
                }*/

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Versión 1.0",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "App para gestión de clubes deportivos",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}