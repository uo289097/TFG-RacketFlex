package com.uniovi.tfg.racketFlex.features.courses.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course

@Composable
fun CourseCard(
    course: Course,
    user: User,
    onJoin: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(course.title)
        Text(course.sport.name)
        Text(course.description)
        Text(course.daysOfWeek.toString())
        Text(course.maxPlayers.toString())
        Text("Inscritos: ${course.players.size}")
        Text("Precio: ${course.price} €/mes")
        Text(course.initDate.toString())
        Text(course.endDate.toString())
        Text(course.startTime.toString())
        Text(course.endTime.toString())
    }


}