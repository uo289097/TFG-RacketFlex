package com.uniovi.tfg.racketFlex.features.courses.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModel
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModelFactory
import com.uniovi.tfg.racketFlex.features.courses.ui.tabs.MyCoursesTab
import com.uniovi.tfg.racketFlex.features.courses.ui.tabs.SearchCoursesTab

@Composable
fun CoursesScreen(
    clubId: String, user: User,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val viewModel: CoursesViewModel = viewModel(
        key = user.email,
        factory = CoursesViewModelFactory(clubId)
    )

    val tabs = listOf("Explorar cursos", "Mis cursos")

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
            0 -> SearchCoursesTab(clubId, user, viewModel)
            1 -> MyCoursesTab(clubId, user, viewModel)
        }
    }
}