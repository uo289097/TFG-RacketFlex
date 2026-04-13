package com.uniovi.tfg.racketFlex.features.courses.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.presentation.CoursesViewModel
import com.uniovi.tfg.racketFlex.features.courses.ui.components.CourseCard
import com.uniovi.tfg.racketFlex.features.courses.ui.components.SportSelectorCourses

@Composable
fun MyCoursesTab(
    clubId: String,
    user: User,
    viewModel: CoursesViewModel
) {

    LaunchedEffect(viewModel.selectedSport) {
        viewModel.loadUserCourses(user.email)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SportSelectorCourses(viewModel)

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.userCourses) { course ->
                CourseCard(
                    course = course,
                    user = user,
                    onJoin = {},
                    onCancel = {
                        viewModel.cancelCourseInscription(course.id, user.email)
                    }
                )
            }
        }
    }
}