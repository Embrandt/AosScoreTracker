package de.embrandt.aostracker.domain.model

data class TurnData(
    val turnNumber: Int,
    val playerData : PlayerTurn,
    val opponentData : PlayerTurn,
    val playerHasFirstTurn: Boolean? = null,
)