package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

sealed class Faction(
    @StringRes val resourceId: Int,
    val battleTactics: List<BattleTactic> = emptyList(),
    val grandStrategies: List<String> = emptyList()
) {
    object Stormcast : Faction(R.string.faction_stormcast)
    object Orruks : Faction(R.string.faction_orruks)
    object Beasts : Faction(R.string.faction_beasts)
    object Khorne : Faction(R.string.faction_khorne)
    object Cities : Faction(R.string.faction_cities)
    object Khaine : Faction(R.string.faction_khaine)
    object Tzeench : Faction(R.string.faction_tzeench)
    object FleshEaters : Faction(R.string.faction_fleshEaters)
    object Fyreslayers : Faction(R.string.faction_fyreslayers)
    object Gitz : Faction(R.string.faction_gitz)
    object Slaanesh : Faction(R.string.faction_slaanesh)
    object Idoneth : Faction(R.string.faction_idoneth)
    object Kharadron : Faction(R.string.faction_kharadron)
    object Belakor : Faction(R.string.faction_belakor)
    object Lumineth : Faction(R.string.faction_lumineth)
    object Nurgle : Faction(R.string.faction_nurgle)
    object Nighthaunt : Faction(R.string.faction_nighthaunt)
    object Ogor : Faction(R.string.faction_ogor)
    object Bonereapers : Faction(R.string.faction_bonereapers)
    object Seraphon : Faction(R.string.faction_seraphon)
    object Skaven : Faction(R.string.faction_skaven)
    object Slaves : Faction(R.string.faction_slaves)
    object Behemat : Faction(R.string.faction_behemat)
    object Gravelords : Faction(R.string.faction_gravelords)
    object Sylvaneth : Faction(R.string.faction_sylvaneth)
}

