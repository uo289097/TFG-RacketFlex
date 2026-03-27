package com.uniovi.tfg.racketFlex.features.matches.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import kotlinx.coroutines.tasks.await

class MatchesRepositoryImpl(
    private val db: FirebaseFirestore
) : MatchesRepository {
    override suspend fun getMatches(clubId: String, sport: Sport): List<Match> {
        val now = Timestamp(System.currentTimeMillis() / 1000, 0)
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .whereEqualTo("sport", sport.name.lowercase())
            .whereGreaterThanOrEqualTo("init_date", now)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Match(
                id = doc.id,
                bookingId = doc.getString("booking_id") ?: "",
                createdBy = doc.getString("created_by") ?: "",
                initDate = doc.getTimestamp("init_date")?.toDate()?.time ?: 0L,
                maxPlayers = doc.getLong("max_players")?.toInt() ?: 0,
                players = doc.get("players") as? List<String> ?: emptyList(),
                sport = sport
            )
        }
    }

    override suspend fun joinMatch(clubId: String, matchId: String, userId: String, index: Int) {
        val doc = db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .document(matchId)
            .get()
            .await()

        val players = (doc.get("players") as? List<String>)?.toMutableList() ?: return
        if (players[index].isNotBlank()) return // ya ocupado por si acaso

        players[index] = userId

        db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .document(matchId)
            .update("players", players)
            .await()
    }
}