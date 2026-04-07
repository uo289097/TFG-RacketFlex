package com.uniovi.tfg.racketFlex.features.booking.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.model.Booking
import com.uniovi.tfg.racketFlex.core.model.BookingInfo
import com.uniovi.tfg.racketFlex.core.model.BookingType
import com.uniovi.tfg.racketFlex.core.model.Court
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.booking.domain.BookingRepository
import kotlinx.coroutines.tasks.await
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

    override suspend fun createMatchBooking(clubId: String, booking: Booking): String {
        val data = hashMapOf(
            "booker_id" to booking.bookerId,
            "court" to booking.court,
            "init_date" to Timestamp(booking.initDate / 1000, 0),
            "end_date" to Timestamp(booking.endDate / 1000, 0),
            "type" to booking.type.name.lowercase()
        )
        val reserva = db.collection("clubs")
            .document(clubId)
            .collection("bookings")
            .add(data)
            .await()
        return reserva.id
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
            snapshot.getField<Int>("number_padel") ?: 0
        )
    }

    override suspend fun getFirstAvailableCourt(
        clubId: String,
        sport: Sport,
        initDate: Long,
        endDate: Long
    ): String? {
        val prefix = if (sport == Sport.TENIS) "tenis" else "padel"
        val field = if (sport == Sport.TENIS) "number_tennis" else "number_padel"

        val clubDoc = db.collection("clubs").document(clubId).get().await()
        val numCourts = clubDoc.getLong(field)?.toInt() ?: return null

        val startTimestamp = Timestamp(initDate / 1000, 0)
        val endTimestamp = Timestamp(endDate / 1000, 0)
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("bookings")
            .whereGreaterThanOrEqualTo("init_date", startTimestamp)
            .whereLessThan("init_date", endTimestamp)
            .get()
            .await()

        val reservedCourts = snapshot.documents.mapNotNull { it.getString("court") }.toSet()

        for (i in 1..numCourts) {
            val courtId = "$prefix${i.toString().padStart(2, '0')}"
            if (courtId !in reservedCourts) return courtId
        }

        return null // todas ocupadas
    }
}