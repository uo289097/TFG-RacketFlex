package com.uniovi.tfg.racketFlex.features.booking.data

import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.domain.BookingRepository
import kotlinx.coroutines.tasks.await

class BookingRepositoryImpl(
    private val db: FirebaseFirestore
) : BookingRepository {

    override suspend fun getCourts(sport: Sport, clubId: String): List<Court> {
        return try {
            val snapshot = db.collection("clubs")
                .document(clubId)
                .get()
                .await()

            val field = if (sport == Sport.TENIS) "number_tennis" else "number_padel"

            val numCourts = snapshot.getLong(field)?.toInt() ?: 0

            val prefix = if (sport == Sport.TENIS) "tenis" else "padel"

            List(numCourts) { index ->
                Court(
                    id = "$prefix${(index + 1).toString().padStart(2, '0')}",
                    sport = sport
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getBookings(dayTimestamp: Long, sport: Sport): List<Booking> {
        val startOfDay = dayTimestamp // Para simplificar, timestamp 00:00
        val endOfDay = dayTimestamp + 24 * 60 * 60 * 1000
        val snapshot = db.collection("bookings")
            .whereGreaterThanOrEqualTo("init_date", startOfDay)
            .whereLessThanOrEqualTo("end_date", endOfDay)
            .get()
            .await()
        return snapshot.documents.map { // TODO Sacar a helpers -> mappers
            Booking(
                bookerId = it.getString("booker_id") ?: "",
                court = it.getString("court") ?: "",
                initDate = it.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = it.getTimestamp("end_date")?.toDate()?.time ?: 0L
            )
        }
    }
}