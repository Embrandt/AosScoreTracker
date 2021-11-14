package de.embrandt.aostracker.domain.model

data class PlayerTurn(
    val scores: List<Score> = emptyList(),
    val battleTactic: String?
)