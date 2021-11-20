package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

enum class BattlePack(
    @StringRes val nameRessource: Int,
    val scoringOptions: List<ScoringOption>,
    val battleTactics: List<BattleTactic>,
    val grandStrategies: List<GrandStrategy>
) {
    Gur(
        nameRessource = R.string.battlePack_gur,
        scoringOptions = listOf(ScoringOption.SlayMonster),
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
