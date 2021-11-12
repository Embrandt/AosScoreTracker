package de.embrandt.aostracker

data class PlayerTurn(
    val scores: List<Score> = emptyList(),
    val battleTactic: String?
)