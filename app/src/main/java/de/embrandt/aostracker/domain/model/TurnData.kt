package de.embrandt.aostracker.domain.model

import androidx.room.*

@Entity
data class TurnData(
    val turnNumber: Int,
    @Embedded(prefix= "player_")
    val playerData : PlayerTurn,
    @Embedded(prefix= "opponent_")
    val opponentData : PlayerTurn,
    val playerHasFirstTurn: Boolean? = null,
    @PrimaryKey(autoGenerate = true)
    val turnId : Long = 0L,
    val gameId : Long = 0L
) {
    constructor(turnNumber: Int, playerHasFirstTurn: Boolean?, turnId: Long, gameId: Long)
            : this(turnNumber, PlayerTurn(), PlayerTurn(), playerHasFirstTurn, turnId, gameId)
}

@Entity
data class GameDataWithTurnScoring (
    @Embedded val gameData : GameData,
    @Relation(parentColumn = "gameId", entityColumn = "gameId")
    val turns : List<TurnData>
)