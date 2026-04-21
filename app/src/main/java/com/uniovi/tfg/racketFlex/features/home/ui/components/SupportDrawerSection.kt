package com.uniovi.tfg.racketFlex.features.home.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SupportDrawerSection(
    onNavigateToAbout: () -> Unit,
) {
    Text(
        text = "Soporte",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    NavigationDrawerItem(
        icon = {
            Icon(
                Icons.AutoMirrored.Filled.ContactSupport,
                contentDescription = null
            )
        },
        label = { Text("Acerca de") },
        selected = false,
        onClick = onNavigateToAbout
    )
}