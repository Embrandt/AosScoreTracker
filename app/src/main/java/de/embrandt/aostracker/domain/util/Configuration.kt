package de.embrandt.aostracker.domain.util

import de.embrandt.aostracker.domain.model.BattlePlan
import de.embrandt.aostracker.domain.model.Faction
import de.embrandt.aostracker.domain.model.ScoringOption

object Configuration {
    val factions : List<Faction> = initializeFactions()
    val battlePlans : List<BattlePlan> = initializeBattlePlans()
}

private fun initializeFactions() : List<Faction> {
    return listOf(
        Faction("Stormcast Eternals"),
        Faction("Orruks")
    )
}

private fun initializeBattlePlans() : List<BattlePlan> {
    return listOf(
        BattlePlan("Machtkampf", setOf(
            ScoringOption("Mindestens 1 Ziel für zwei eigene Züge gehalten", 1),
            ScoringOption("2 oder mehr Ziele für zwei eigene Züge gehalten", 1),
            ScoringOption("Mehr Ziele kontrolliert als der Gegner", 1),
            ScoringOption("Taktisches Vorhaben erfüllt", 2)
        )),
        BattlePlan("Schraubstock", BPScoring.DefaultScoring.scoringOptions),
        BattlePlan("Brutale Eroberung", setOf(
            ScoringOption("Ziel an Grenze des eigenen Territoriums gehalten", 1),
            ScoringOption("Neutrales Ziel gehalten", 2),
            ScoringOption("Ziel an Grenze des generischen Territoriums gehalten", 4),
            ScoringOption("Taktisches Vorhaben erfüllt", 2)
            )
        )
    )
}
sealed class BPScoring(val scoringOptions : Set<ScoringOption>) {
    object DefaultScoring : BPScoring(setOf(
        ScoringOption("Hold 1", 1),
        ScoringOption("Hold 2+", 1),
        ScoringOption("Hold More", 1),
        ScoringOption("Battle tactic scored", 2)
    ))
}