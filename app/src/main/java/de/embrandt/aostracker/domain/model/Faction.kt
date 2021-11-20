package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

enum class Faction(
    @StringRes val resourceId: Int,
    val battleTactics: List<BattleTactic> = emptyList(),
    val grandStrategies: List<GrandStrategy> = emptyList()
) {
    Stormcast(R.string.faction_stormcast),
    Orruks(R.string.faction_orruks),
    Beasts(R.string.faction_beasts),
    Khorne(R.string.faction_khorne),
    Cities(R.string.faction_cities),
    Khaine(R.string.faction_khaine),
    Tzeench(R.string.faction_tzeench),
    FleshEaters(R.string.faction_fleshEaters),
    Fyreslayers(R.string.faction_fyreslayers),
    Gitz(R.string.faction_gitz),
    Slaanesh(R.string.faction_slaanesh),
    Idoneth(R.string.faction_idoneth),
    Kharadron(R.string.faction_kharadron),
    Belakor(R.string.faction_belakor),
    Lumineth(R.string.faction_lumineth),
    Nurgle(R.string.faction_nurgle),
    Nighthaunt(R.string.faction_nighthaunt),
    Ogor(R.string.faction_ogor),
    Bonereapers(R.string.faction_bonereapers),
    Seraphon(R.string.faction_seraphon),
    Skaven(R.string.faction_skaven),
    Slaves(R.string.faction_slaves),
    Behemat(R.string.faction_behemat),
    Gravelords(R.string.faction_gravelords),
    Sylvaneth(R.string.faction_sylvaneth),
}

