package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

enum class BattleTactic (@StringRes val nameResource : Int, @StringRes val explanation : Int, val extraScore : ScoringOption? = null) {
    BrokenRanks(R.string.battleTactic_brokenRanks, R.string.notImplemented, ScoringOption.SlayWithMonster),
    Conquer (R.string.battleTactic_conquer, R.string.notImplemented),
    SlayTheWarlord (R.string.battleTactic_slayTheWarlord, R.string.notImplemented,ScoringOption.SlayWithMonster),
    Advance (R.string.battleTactic_advance, R.string.notImplemented, ScoringOption.RunWithMonster),
    BringItDown (R.string.battleTactic_bringItDown, R.string.notImplemented,ScoringOption.SlayWithMonster),
    Expansion (R.string.battleTactic_expansion, R.string.notImplemented),
    TakeOver (R.string.battleTactic_takeOver, R.string.notImplemented),
    Spearhead (R.string.battleTactic_spearhead, R.string.notImplemented,ScoringOption.HoldWithMonster)
}