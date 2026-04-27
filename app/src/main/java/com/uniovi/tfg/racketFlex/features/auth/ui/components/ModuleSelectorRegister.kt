package com.uniovi.tfg.racketFlex.features.auth.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.uniovi.tfg.racketFlex.core.model.ClubModule
import com.uniovi.tfg.racketFlex.features.auth.presentation.RegisterClubViewModel

@Composable
fun ModuleSelectorRegister(viewModel: RegisterClubViewModel) {
    ClubModule.entries.forEach { module ->
        FilterChip(
            selected = viewModel.selectedModules.contains(module),
            onClick = {
                viewModel.selectedModules = if (module in viewModel.selectedModules)
                    viewModel.selectedModules - module
                else
                    viewModel.selectedModules + module
            },
            label = {
                Text(
                    module.name.uppercase()
                )
            },
            leadingIcon = if (viewModel.selectedModules.contains(module)) {
                { Icon(Icons.Default.Check, contentDescription = null) }
            } else null
        )
    }
}