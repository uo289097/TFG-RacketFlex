package com.uniovi.tfg.racketFlex.features.home.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.uniovi.tfg.racketFlex.core.network.toClubModule
import com.uniovi.tfg.racketFlex.features.home.domain.ClubInfo
import com.uniovi.tfg.racketFlex.features.home.domain.ClubInfoBack
import com.uniovi.tfg.racketFlex.features.home.domain.ConfigRepository
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapNotNull

class ConfigRepositoryImpl(
    private val db: FirebaseFirestore
) : ConfigRepository {

    override suspend fun getClubInfo(clubId: String): ClubInfo {
        val doc = db.collection("clubs")
            .document(clubId)
            .get()
            .await()

        return ClubInfo(
            name = doc.getString("nombre") ?: "",
            bookingDuration = doc.getField<Int>("booking_duration") ?: 0,
            bookingPrice = doc.getDouble("booking_price") ?: 0.0,
            maxBookingsPerDay = doc.getField<Int>("max_bookings_per_day") ?: 0,
            modules = (doc.get("modulos") as? List<*>)?.mapNotNull { it as? String }
                ?.mapNotNull { it.toClubModule() } ?: emptyList(),
            numberPadel = doc.getField<Int>("number_padel") ?: 0,
            numberTenis = doc.getField<Int>("number_tennis") ?: 0,
            openTime = doc.getField<Int>("open_time") ?: 0,
            closeTime = doc.getField<Int>("close_time") ?: 0
        )
    }

    override suspend fun updateClub(clubId: String, club: ClubInfoBack) {
        db.collection("clubs")
            .document(clubId)
            .set(club)
            .await()


    }

}