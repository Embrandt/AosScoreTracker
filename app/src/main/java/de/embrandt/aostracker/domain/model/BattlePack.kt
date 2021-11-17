package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes

sealed class BattlePack(
    @StringRes val nameRessource: Int,
    val scoringOptions: List<ScoringOption> = emptyList(),
    val battleTactics: List<BattleTactic>,
    val grandStrategies: List<GrandStrategy>
) {
    object Gur : BattlePack(
        nameRessource = 0,
        battleTactics = listOf(
            BattleTactic.BrokenRanks,
            BattleTactic.Conquer,
            BattleTactic.Advance,
            BattleTactic.BringItDown,
            BattleTactic.Expansion,
            BattleTactic.SlayTheWarlord,
            BattleTactic.Spearhead,
            BattleTactic.TakeOver
        ),
        grandStrategies = listOf(GrandStrategy.PrizedSorcery)
    )
}
