package com.uniovi.tfg.racketFlex.features.matches.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModel
import com.uniovi.tfg.racketFlex.features.matches.presentation.MatchesViewModelFactory
import com.uniovi.tfg.racketFlex.features.matches.ui.tabs.MyMatchesTab
import com.uniovi.tfg.racketFlex.features.matches.ui.tabs.SearchMatchesTab

@Composable
fun MatchesScreen(clubId: String, user: User) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val viewModel: MatchesViewModel = viewModel(
        key = user.email,
        factory = MatchesViewModelFactory(clubId)
    )

    val tabs = listOf("Explorar partidos", "Mis partidos", "Crear partido")

    Column(modifier = Modifier.fillMaxWidth()) {
        SecondaryTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> SearchMatchesTab(clubId, user, viewModel)
            1 -> MyMatchesTab(clubId, user, viewModel)
            2 -> Text("Crear partido")
        }
    }


}