package de.embrandt.aostracker.domain.model

data class PlayerTurn(
    val scores: Set<ScoringOption> = emptySet(),
    val battleTactic: BattleTactic? = null,
    val commandPointsGained : Int = 0,
    val commandPointsSpent : Int = 0
)