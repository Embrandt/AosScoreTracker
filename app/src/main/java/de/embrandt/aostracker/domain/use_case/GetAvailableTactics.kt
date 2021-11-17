package de.embrandt.aostracker.domain.use_case

import de.embrandt.aostracker.domain.model.BattlePack
import de.embrandt.aostracker.domain.model.BattleTactic
import de.embrandt.aostracker.domain.model.Faction

class GetAvailableTactics {
    operator fun invoke(faction: Faction?, battlePack: BattlePack?) : List<BattleTactic> {
        val availableTactics = mutableListOf<BattleTactic>()
        faction?.let{availableTactics.addAll(it.battleTactics)}
        battlePack?.let{availableTactics.addAll(it.battleTactics)}
        return availableTactics
    }
}