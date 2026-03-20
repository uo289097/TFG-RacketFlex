package com.uniovi.tfg.racketFlex.features.booking.data

import android.util.Log
import androidx.compose.ui.text.toUpperCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.BookingType
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.domain.BookingRepository
import kotlinx.coroutines.tasks.await
import java.util.Locale
import java.util.Locale.getDefault

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

    override suspend fun getBookings(
        dayTimestamp: Long,
        sport: Sport,
        clubId: String
    ): List<Booking> {
        val startOfDay = Timestamp(dayTimestamp / 1000, 0)
        val endOfDay = Timestamp((dayTimestamp / 1000) + 24 * 60 * 60, 0)
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("bookings")
            .whereGreaterThanOrEqualTo("init_date", startOfDay)
            .whereLessThanOrEqualTo("end_date", endOfDay)
            .get()
            .await()
        return snapshot.documents.map {
            Booking(
                bookerId = it.getString("booker_id") ?: "",
                court = it.getString("court") ?: "",
                initDate = it.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                endDate = it.getTimestamp("end_date")?.toDate()?.time ?: 0L,
                type = BookingType.valueOf(it.getString("type")!!.uppercase(getDefault()))
            )
        }
    }

    override suspend fun createBooking(clubId: String, booking: Booking) {
        val data = hashMapOf(
            "booker_id" to booking.bookerId,
            "court" to booking.court,
            "init_date" to Timestamp(booking.initDate / 1000, 0),
            "end_date" to Timestamp(booking.endDate / 1000, 0),
            "type" to booking.type.name.lowercase()
        )
        db.collection("clubs")
            .document(clubId)
            .collection("bookings")
            .add(data)
            .await()
    }
}