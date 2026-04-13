package com.uniovi.tfg.racketFlex.features.courses.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CourseCard(
    course: Course,
    user: User,
    onJoin: () -> Unit,
) {
    val locale = Locale.getDefault()
    val start = Instant.ofEpochMilli(course.initDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val end = Instant.ofEpochMilli(course.endDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val dateText = if (start.month == end.month) {
        "${start.dayOfMonth}–${end.dayOfMonth} ${
            start.month.getDisplayName(TextStyle.SHORT, locale).uppercase()
        }"
    } else {
        "${start.dayOfMonth} ${
            start.month.getDisplayName(TextStyle.SHORT, locale).uppercase()
        } – ${end.dayOfMonth} ${
            end.month.getDisplayName(TextStyle.SHORT, locale).uppercase()
        }"
    }

    val priceText = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-ES"))
        .apply { minimumFractionDigits = 2 }
        .format(course.price)
    val daysText = course.daysOfWeek.joinToString(", ") {
        it.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(course.title, style = MaterialTheme.typography.titleMedium)
                Text(dateText, style = MaterialTheme.typography.labelMedium)
            }

            Spacer(Modifier.height(6.dp))

            Text(
                course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("🗓 $daysText")
                    Text("⏰ ${course.startTime} - ${course.endTime}")
                }
                Column {
                    Text("👥 ${course.maxPlayers - course.players.size} plazas")
                    Text("💰 $priceText €/mes")
                }
            }

            Spacer(Modifier.height(12.dp))
            // TODO Cambiar
            Button(
                onClick = { Log.d("courses", "Curso: {${course.title}}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inscribirme")
            }
        }
    }
}

/*
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
Text(course.endTime.toString())*/