package de.embrandt.aostracker

data class TurnData(
    val turnNumber: Int,
    val playerData : PlayerTurn,
    val opponentData : PlayerTurn,
    val playerHasFirstTurn: Boolean? = true,
)