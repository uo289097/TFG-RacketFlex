package com.uniovi.tfg.racketFlex.features.courses.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.model.BookingInfo
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
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                maxPlayers = doc.getField<Int>("max_players") ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport,
                price = doc.getDouble("price") ?: 0.0,
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = doc.getTimestamp("end_date")?.toDate()?.time ?: 0L,
                daysOfWeek = (doc.get("daysOfWeek") as? List<String>)
                    ?.mapNotNull { day ->
                        try {
                            DayOfWeek.valueOf(day)
                        } catch (_: Exception) {
                            null
                        }
                    } ?: emptyList(),
                startTime = doc.getString("start_time")
                    ?.let { LocalTime.parse(it, formatter) },
                endTime = doc.getString("end_time")
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

        val players = (doc.get("players") as? List<String>)?.toMutableList() ?: mutableListOf()
        if (players.contains(userId)) return
        val maxPlayers = doc.getField<Int>("max_players") ?: return
        if (players.size >= maxPlayers) return

        players.add(userId)

        db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .update("players", FieldValue.arrayUnion(userId))
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
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                maxPlayers = doc.getField<Int>("max_players") ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport,
                price = doc.getDouble("price") ?: 0.0,
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = doc.getTimestamp("end_date")?.toDate()?.time ?: 0L,
                daysOfWeek = (doc.get("daysOfWeek") as? List<String>)
                    ?.mapNotNull { day ->
                        try {
                            DayOfWeek.valueOf(day)
                        } catch (_: Exception) {
                            null
                        }
                    } ?: emptyList(),
                startTime = doc.getString("start_time")
                    ?.let { LocalTime.parse(it, formatter) },
                endTime = doc.getString("end_time")
                    ?.let { LocalTime.parse(it, formatter) },
            )
        }
    }

    override suspend fun cancelCourseInscription(
        clubId: String,
        userId: String,
        courseId: String
    ) {
        val doc = db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .get()
            .await()

        val players = (doc.get("players") as? List<String>)?.toMutableList() ?: mutableListOf()
        if (!players.contains(userId)) return

        players.remove(userId)

        db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .update("players", FieldValue.arrayRemove(userId))
            .await()
    }

    override suspend fun loadAdminCourses(clubId: String, sport: Sport): List<Course> {
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .whereEqualTo("sport", sport.name.lowercase())
            .orderBy("init_date", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Course(
                id = doc.id,
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                maxPlayers = doc.getField<Int>("max_players") ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport,
                price = doc.getDouble("price") ?: 0.0,
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = doc.getTimestamp("end_date")?.toDate()?.time ?: 0L,
                daysOfWeek = (doc.get("daysOfWeek") as? List<String>)
                    ?.mapNotNull { day ->
                        try {
                            DayOfWeek.valueOf(day)
                        } catch (_: Exception) {
                            null
                        }
                    } ?: emptyList(),
                startTime = doc.getString("start_time")
                    ?.let { LocalTime.parse(it, formatter) },
                endTime = doc.getString("end_time")
                    ?.let { LocalTime.parse(it, formatter) },
            )
        }
    }

    override suspend fun createCourse(clubId: String, course: Course) {

        val data = hashMapOf(
            "description" to course.description,
            "price" to course.price,
            "max_players" to course.maxPlayers,
            "sport" to course.sport.toString().lowercase(),
            "players" to course.players,
            "title" to course.title,
            "start_time" to course.startTime.toString(),
            "end_time" to course.endTime.toString(),
            "daysOfWeek" to course.daysOfWeek,
            "init_date" to Timestamp(course.initDate / 1000, 0),
            "end_date" to Timestamp(course.endDate / 1000, 0),
        )

        db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .add(data)
            .await()
    }

    override suspend fun removeCourse(clubId: String, courseId: String) {
        db.collection("clubs")
            .document(clubId)
            .collection("courses")
            .document(courseId)
            .delete()
            .await()

    }

    override suspend fun getBookingInfo(clubId: String): BookingInfo {
        val snapshot = db.collection("clubs")
            .document(clubId)
            .get()
            .await()
        return BookingInfo(
            snapshot.getField<Int>("booking_duration") ?: 0,
            snapshot.getField<Int>("open_time") ?: 0,
            snapshot.getField<Int>("close_time") ?: 0,
            snapshot.getField<Int>("number_tennis") ?: 0,
            snapshot.getField<Int>("number_padel") ?: 0,
            snapshot.getField<Int>("max_bookings_per_day") ?: 0
        )
    }


    // TODO SACAR A MAPPER
    fun String.toSport(): Sport? {
        return runCatching { Sport.valueOf(this.uppercase()) }.getOrNull()
    }
}