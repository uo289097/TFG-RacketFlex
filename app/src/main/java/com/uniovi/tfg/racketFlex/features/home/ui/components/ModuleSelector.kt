package com.uniovi.tfg.racketFlex.features.home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.features.home.presentation.ConfigViewModel

@Composable
fun ModuleSelector(viewModel: ConfigViewModel) {
    ClubModule.entries.forEach { module ->
        FilterChip(
            selected = viewModel.modules.contains(module),
            onClick = { viewModel.toggleModule(module) },
            label = {
                Text(
                    module.name.uppercase()
                )
            },
            leadingIcon = if (viewModel.modules.contains(module)) {
                { Icon(Icons.Default.Check, contentDescription = null) }
            } else null
        )
    }
}