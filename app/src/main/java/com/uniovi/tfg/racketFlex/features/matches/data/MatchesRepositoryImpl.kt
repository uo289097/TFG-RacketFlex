package com.uniovi.tfg.racketFlex.features.matches.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.uniovi.tfg.racketFlex.core.model.Sport
import com.uniovi.tfg.racketFlex.features.matches.domain.MatchesRepository
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.Match
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.SetScore
import com.uniovi.tfg.racketFlex.features.matches.domain.entities.TennisMatchType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
                sport = sport,
                score = (doc.get("score") as? List<Map<String, Long>>)?.map {
                    SetScore(
                        player1 = it["player1"]?.toInt() ?: 0,
                        player2 = it["player2"]?.toInt() ?: 0
                    )
                } ?: emptyList(),
                playersNames = emptyList()
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
        if (players[index].isNotBlank()) return

        players[index] = userId

        db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .document(matchId)
            .update("players", players)
            .await()
    }

    override suspend fun getUserMatches(clubId: String, sport: Sport, userId: String): List<Match> {
        val snapshot = db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .whereEqualTo("sport", sport.name.lowercase())
            .whereArrayContains("players", userId)
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
                sport = sport,
                score = (doc.get("score") as? List<Map<String, Long>>)?.map {
                    SetScore(
                        player1 = it["player1"]?.toInt() ?: 0,
                        player2 = it["player2"]?.toInt() ?: 0
                    )
                } ?: emptyList(),
                playersNames = emptyList()
            )
        }
    }

    override suspend fun addScore(clubId: String, matchId: String, score: List<SetScore>) {
        val scoreData = score.map { mapOf("player1" to it.player1, "player2" to it.player2) }
        db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .document(matchId)
            .update("score", scoreData)
            .await()
    }

    override suspend fun createMatch(
        clubId: String,
        userId: String,
        match: Match
    ) {
        val data = hashMapOf(
            "booking_id" to match.bookingId,
            "created_by" to match.createdBy,
            "init_date" to Timestamp(match.initDate / 1000, 0),
            "max_players" to match.maxPlayers,
            "sport" to match.sport.toString().lowercase(),
            "players" to match.players,
            "score" to match.score
        )

        db.collection("clubs")
            .document(clubId)
            .collection("matches")
            .add(data)
            .await()
    }

    override suspend fun getPlayerNames(playersNames: List<String>): List<String> {
        return coroutineScope {
            playersNames.map { player ->
                async {
                    if (player.isEmpty()) ""
                    else {
                        val doc = db.collection("users")
                            .document(player)
                            .get()
                            .await()
                        doc.getString("nombre") ?: ""
                    }
                }
            }.awaitAll()
        }
    }

}