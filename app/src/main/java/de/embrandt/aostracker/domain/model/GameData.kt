package de.embrandt.aostracker.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class GameData(
    val battleDate: LocalDate?,
    val battlePack: BattlePack? = BattlePack.Gur,
    val battlePlan: BattlePlan? = null,
    val playerName: String = "",
    val opponentName: String = "",
    val playerFaction: Faction? = null,
    val playerGrandStrategy: String = "",
    val opponentFaction: Faction? = null,
    val opponentGrandStrategy: String = ""
) {
    private val newFormat = DateTimeFormatter.ISO_LOCAL_DATE
    val battleDateText : String
        get() = battleDate?.format(newFormat) ?: ""
}
