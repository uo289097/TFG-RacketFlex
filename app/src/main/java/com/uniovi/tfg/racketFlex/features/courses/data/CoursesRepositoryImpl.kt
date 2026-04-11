package com.uniovi.tfg.racketFlex.features.courses.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.courses.domain.CoursesRepository
import com.uniovi.tfg.racketFlex.features.courses.domain.entities.Course
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CoursesRepositoryImpl(
    private val db: FirebaseFirestore
) : CoursesRepository {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    override suspend fun getCourses(
        clubId: String,
        sport: Sport
    ): List<Course> {
        val now = Timestamp(System.currentTimeMillis() / 1000, 0)
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .whereEqualTo("sport", sport.name.lowercase())
            .whereGreaterThanOrEqualTo("init_date", now)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Course(
                id = doc.id,
                maxPlayers = doc.getField<Int>("max_players") ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport,
                price = doc.getDouble("price") ?: 0.0,
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                description = doc.getString("description") ?: "",
                daysOfWeek = (doc.get("daysOfWeek") as? List<String>)
                    ?.mapNotNull { day ->
                        try {
                            DayOfWeek.valueOf(day)
                        } catch (_: Exception) {
                            null
                        }
                    } ?: emptyList(),
                startTime = doc.getString("startTime")
                    ?.let { LocalTime.parse(it, formatter) },
                endTime = doc.getString("endTime")
                    ?.let { LocalTime.parse(it, formatter) },
            )
        }
    }


    override suspend fun joinCourse(
        clubId: String,
        courseId: String,
        userId: String,
    ) {
        val doc = db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .get()
            .await()

        val players = (doc.get("players") as? List<String>)?.toMutableList() ?: return
        val maxPlayers = doc.getField<Int>("max_players") ?: return
        if (players.size >= maxPlayers) return

        players.add(userId)

        db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .update("players", players)
            .await()
    }

    override suspend fun getUserCourses(
        clubId: String,
        sport: Sport,
        userId: String
    ): List<Course> {
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .whereEqualTo("sport", sport.name.lowercase())
            .whereArrayContains("players", userId)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Course(
                id = doc.id,
                maxPlayers = doc.getField<Int>("max_players") ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport,
                price = doc.getDouble("price") ?: 0.0,
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                description = doc.getString("description") ?: "",
                daysOfWeek = (doc.get("daysOfWeek") as? List<String>)
                    ?.mapNotNull { day ->
                        try {
                            DayOfWeek.valueOf(day)
                        } catch (e: Exception) {
                            null
                        }
                    } ?: emptyList(),
                startTime = doc.getString("startTime")
                    ?.let { LocalTime.parse(it, formatter) },
                endTime = doc.getString("endTime")
                    ?.let { LocalTime.parse(it, formatter) },
            )
        }
    }
}