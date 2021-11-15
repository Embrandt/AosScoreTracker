package de.embrandt.aostracker.domain.model

data class Faction(
    val name: String,
    val battleTactics: List<BattleTactic> = emptyList(),
    val grandStrategies: List<String> = emptyList()
)

