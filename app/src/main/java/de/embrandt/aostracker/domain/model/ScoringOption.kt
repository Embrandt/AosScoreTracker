package de.embrandt.aostracker.domain.model

enum class ScoringOption(
    val shortDescripton: String,
    val pointValue: Int,
    val extraPoint: List<ScoringOption> = emptyList()
) {
    Hold1("Hold 1", 1),
    Hold2("Hold 2+", 1),
    HoldMore("Mehr Ziele kontrolliert als der Gegner", 1),
    BattleTactic("Taktisches Vorhaben erfüllt", 2),
    Hold1Consecutive("Mindestens 1 Ziel für zwei eigene Züge gehalten", 1),
    Hold2Consecutive("2 oder mehr Ziele für zwei eigene Züge gehalten", 1),
    HoldOwn("Ziel an Grenze des eigenen Territoriums gehalten", 1),
    HoldNeutral("Neutrales Ziel gehalten", 2),
    HoldEnemy("Ziel an Grenze des generischen Territoriums gehalten", 4),
    SlayMonster("Monster getötet", 1),
    SlayWithMonster("Mit Monster getötet", 1),
    RunWithMonster("Alle drei waren Monster", 1),
    HoldWithMonster("Mindestens zwei Monster dabei", 1)
}
