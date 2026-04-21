package com.uniovi.tfg.racketFlex.features.courses.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uniovi.tfg.racketFlex.core.model.User
import com.uniovi.tfg.racketFlex.core.model.UserRole
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CourseCard(
    course: Course,
    user: User,
    onJoin: () -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    val locale = Locale.getDefault()
    val start = Instant.ofEpochMilli(course.initDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val end = Instant.ofEpochMilli(course.endDate)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val dateText = remember(course.initDate, course.endDate) {
        if (start.month == end.month) {
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
    }

    val priceText = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-ES"))
        .apply { minimumFractionDigits = 2 }
        .format(course.price)
    val daysText = course.daysOfWeek.joinToString(", ") {
        it.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"))
    }

    val isFinished = course.endDate < System.currentTimeMillis()
    val hasNotStarted = course.initDate > System.currentTimeMillis()
    val alreadyJoined = user.email in course.players
    val isInProgress =
        course.initDate <= System.currentTimeMillis() && course.endDate >= System.currentTimeMillis()

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
                    Text("👥 ${maxOf(0, course.maxPlayers - course.players.size)} plazas")
                    Text("💰 $priceText €/mes")
                }
            }

            Spacer(Modifier.height(12.dp))
            if (user.role == UserRole.ADMIN) {
                if (course.players.isEmpty())
                    Text("Sin inscritos")
                else
                    Text("Inscritos: ${course.players}")
                Spacer(Modifier.height(5.dp))
                Button(
                    onClick = { onRemove() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar curso")
                }
            } else {
                if ((hasNotStarted && !alreadyJoined) || (isInProgress && !alreadyJoined)) {
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Inscribirme")
                    }
                } else if (isFinished) {
                    Text(
                        "Finalizado",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Button(
                        onClick = { showCancelDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar inscripción")
                    }
                }
            }
        }
    }
    if (showConfirmDialog) {
        ConfirmInscriptionDialog(
            course = course,
            dateText = dateText,
            onConfirm = {
                onJoin()
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }
    if (showCancelDialog) {
        ConfirmCancelDialog(
            course = course,
            dateText = dateText,
            onConfirm = {
                onCancel()
                showCancelDialog = false
            },
            onDismiss = {
                showCancelDialog = false
            }
        )
    }
}