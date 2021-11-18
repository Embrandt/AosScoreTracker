package de.embrandt.aostracker.domain.model

sealed class ScoringOption (val shortDescripton : String, val pointValue : Int, val extraPoint : List<ScoringOption> = emptyList()) {
    object Hold1 : ScoringOption("Hold 1", 1)
    object Hold2 : ScoringOption("Hold 2+", 1)
    object HoldMore : ScoringOption("Mehr Ziele kontrolliert als der Gegner", 1)
    object BattleTactic : ScoringOption("Taktisches Vorhaben erfüllt", 2)
    object Hold1Consecutive : ScoringOption("Mindestens 1 Ziel für zwei eigene Züge gehalten", 1)
    object Hold2Consecutive : ScoringOption("2 oder mehr Ziele für zwei eigene Züge gehalten", 1)
    object HoldOwn : ScoringOption("Ziel an Grenze des eigenen Territoriums gehalten", 1)
    object HoldNeutral : ScoringOption("Neutrales Ziel gehalten", 2)
    object HoldEnemy : ScoringOption("Ziel an Grenze des generischen Territoriums gehalten", 4)
    object SlayMonster : ScoringOption("Monster getötet", 1)
}
