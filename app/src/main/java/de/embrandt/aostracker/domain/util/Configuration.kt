package de.embrandt.aostracker.domain.util

import de.embrandt.aostracker.domain.model.BattlePlan
import de.embrandt.aostracker.domain.model.Faction

object Configuration {
    val factions: List<Faction> = initializeFactions()
    val battlePlans: List<BattlePlan> = initializeBattlePlans()
}

private fun initializeFactions(): List<Faction> {
    return listOf(
        Faction.Stormcast,
        Faction.Orruks,
        Faction.Beasts,
        Faction.Khorne,
        Faction.Cities,
        Faction.Khaine,
        Faction.Tzeench,
        Faction.FleshEaters,
        Faction.Fyreslayers,
        Faction.Gitz,
        Faction.Slaanesh,
        Faction.Idoneth,
        Faction.Kharadron,
        Faction.Belakor,
        Faction.Lumineth,
        Faction.Nurgle,
        Faction.Nighthaunt,
        Faction.Ogor,
        Faction.Bonereapers,
        Faction.Seraphon,
        Faction.Skaven,
        Faction.Slaves,
        Faction.Behemat,
        Faction.Gravelords,
        Faction.Sylvaneth
    )
}

private fun initializeBattlePlans(): List<BattlePlan> {
    return listOf(
        BattlePlan.PowerStruggle,
        BattlePlan.TheVice,
        BattlePlan.SavageGains
    )
}