package com.uniovi.tfg.racketFlex.features.matches.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.ui.components.SportSelectorMatches

@Composable
fun SearchMatchesTab(clubId: String, user: User, viewModel: MatchesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        SportSelectorMatches()

    }

    ExtendedFloatingActionButton(
        text = { Text("Crear partido") },
        icon = { Icon(Icons.Filled.Add, "Crear partido") },
        onClick = { },
    )

}


