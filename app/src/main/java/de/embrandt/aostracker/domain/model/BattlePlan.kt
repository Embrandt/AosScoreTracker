package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R
import de.embrandt.aostracker.domain.model.ScoringOption.*

sealed class BattlePlan(@StringRes val nameRessource: Int, val scoringOptions : Set<ScoringOption>) {
    object PowerStruggle : BattlePlan(
        nameRessource = R.string.battlePlan_powerStruggle,
        scoringOptions = setOf(Hold1Consecutive, Hold2Consecutive, HoldMore, BattleTactic)
    )
    object TheVice : BattlePlan(
        nameRessource = R.string.battlePlan_vice,
        scoringOptions = setOf(Hold1, Hold2, HoldMore, BattleTactic)
    )
    object SavageGains : BattlePlan(
        nameRessource = R.string.battlePlan_savageGains,
        scoringOptions = setOf(
            HoldOwn, HoldNeutral, HoldEnemy, BattleTactic
        )
    )
}
