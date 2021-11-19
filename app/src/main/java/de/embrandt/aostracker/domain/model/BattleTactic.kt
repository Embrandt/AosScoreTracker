package de.embrandt.aostracker.domain.model

enum class BattleTactic (val nameId : String, val explanation : String = "") {
    BrokenRanks("Ihre Reihen Zerschmettern"),
    Conquer ("Erobern"),
    SlayTheWarlord ("Den Kriegsherr töten"),
    Advance ("Entschlossener Vorstoß"),
    BringItDown ("Bringt es zur Strecke"),
    Expansion ("Aggresive Expansion"),
    TakeOver ("Monströse Übernahme"),
    Spearhead ("Wilde Speerspitze")
}