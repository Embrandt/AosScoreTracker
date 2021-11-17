package de.embrandt.aostracker.domain.model

sealed class BattleTactic (val name : String, val explanation : String = "") {
    object BrokenRanks : BattleTactic("Ihre Reihen Zerschmettern")
    object Conquer : BattleTactic("Erobern")
    object SlayTheWarlord : BattleTactic("Den Kriegsherr töten")
    object Advance : BattleTactic("Entschlossener Vorstoß")
    object BringItDown : BattleTactic("Bringt es zur Strecke")
    object Expansion : BattleTactic("Aggresive Expansion")
    object TakeOver : BattleTactic("Monströse Übernahme")
    object Spearhead : BattleTactic("Wilde Speerspitze")
}