package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R
import de.embrandt.aostracker.domain.model.ScoringOption.*
import de.embrandt.aostracker.domain.model.ScoringOption.BattleTactic

enum class BattlePlan(@StringRes val nameRessource: Int, val scoringOptions: Set<ScoringOption>) {
    PowerStruggle(
        nameRessource = R.string.battlePlan_powerStruggle,
        scoringOptions = setOf(Hold1Consecutive, Hold2Consecutive, HoldMore, BattleTactic)
    ),
    TheVice(
        nameRessource = R.string.battlePlan_vice,
        scoringOptions = setOf(Hold1, Hold2, HoldMore, BattleTactic)
    ),
    SavageGains(
        nameRessource = R.string.battlePlan_savageGains,
        scoringOptions = setOf(HoldOwn, HoldNeutral, HoldEnemy, BattleTactic)
    )
}
