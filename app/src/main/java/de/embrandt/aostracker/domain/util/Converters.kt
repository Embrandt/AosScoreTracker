package de.embrandt.aostracker.domain.util

import androidx.room.TypeConverter
import de.embrandt.aostracker.R
import de.embrandt.aostracker.domain.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.let {
            val newFormat = DateTimeFormatter.ISO_LOCAL_DATE
            it.format(newFormat)
        }
    }

    @TypeConverter
    fun fromDateString(string: String?): LocalDate? {
        return string?.let {
            LocalDate.parse(it)
        }
    }

    @TypeConverter
    fun toString(battlePack: BattlePack?): Int? {
        return battlePack?.nameRessource
    }

    @TypeConverter
    fun fromRessourceId(id: Int?): BattlePack? {
        return id?.let {
            when (id) {
                BattlePack.Gur.nameRessource -> BattlePack.Gur
                else -> throw IllegalArgumentException("Unknown BattlePack")
            }
        }
    }

    @TypeConverter
    fun toString(battlePlan: BattlePlan?): Int? {
        return battlePlan?.nameRessource
    }

    @TypeConverter
    fun battlePlanFromRessourceId(id: Int?): BattlePlan? {
        return id?.let {
            when (it) {
                R.string.battlePlan_powerStruggle -> BattlePlan.PowerStruggle
                R.string.battlePlan_savageGains -> BattlePlan.SavageGains
                R.string.battlePlan_vice -> BattlePlan.TheVice
                else -> throw IllegalArgumentException(" Unknown battle plan")
            }
        }
    }

    @TypeConverter
    fun toString(faction: Faction?): Int? {
        return faction?.resourceId
    }

    @TypeConverter
    fun fromFactionString(name: Int?): Faction? {
        return name?.let {
            when (name) {
                R.string.faction_stormcast -> Faction.Stormcast
                R.string.faction_orruks -> Faction.Orruks
                R.string.faction_beasts -> Faction.Beasts
                R.string.faction_khorne -> Faction.Khorne
                R.string.faction_cities -> Faction.Cities
                R.string.faction_khaine -> Faction.Khaine
                R.string.faction_tzeench -> Faction.Tzeench
                R.string.faction_fleshEaters -> Faction.FleshEaters
                R.string.faction_fyreslayers -> Faction.Fyreslayers
                R.string.faction_gitz -> Faction.Gitz
                R.string.faction_slaanesh -> Faction.Slaanesh
                R.string.faction_idoneth -> Faction.Idoneth
                R.string.faction_kharadron -> Faction.Kharadron
                R.string.faction_belakor -> Faction.Belakor
                R.string.faction_lumineth -> Faction.Lumineth
                R.string.faction_nurgle -> Faction.Nurgle
                R.string.faction_nighthaunt -> Faction.Nighthaunt
                R.string.faction_ogor -> Faction.Ogor
                R.string.faction_bonereapers -> Faction.Bonereapers
                R.string.faction_seraphon -> Faction.Seraphon
                R.string.faction_skaven -> Faction.Skaven
                R.string.faction_slaves -> Faction.Slaves
                R.string.faction_behemat -> Faction.Behemat
                R.string.faction_gravelords -> Faction.Gravelords
                R.string.faction_sylvaneth -> Faction.Sylvaneth
                else -> throw IllegalArgumentException("Unknown faction id")
            }
        }
    }

    @TypeConverter
    fun scoreListToString(scores: Set<ScoringOption>): String {
        var databaseString = ""
        for (score in scores) {
            databaseString += score.name
            databaseString += ","
        }
        return databaseString.dropLast(1)
    }

    @TypeConverter
    fun fromDatabaseString(databaseString: String): Set<ScoringOption> {
        if (databaseString.isEmpty()) {
            return emptySet()
        }
        return databaseString.split(",").map {
            ScoringOption.valueOf(it)
        }.toSet()
    }
}