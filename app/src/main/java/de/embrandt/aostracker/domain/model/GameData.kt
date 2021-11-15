package de.embrandt.aostracker.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class GameData(
    var battleDate: LocalDate?,
    var playerName: String = "",
    var opponentName: String = "",
    var playerFaction: Faction? = null,
    var playerGrandStrategy: String = "",
    var opponentFaction: Faction? = null,
    var opponentGrandStrategy: String = ""
) {
    private val newFormat = DateTimeFormatter.ISO_LOCAL_DATE
    val battleDateText : String
        get() = battleDate?.format(newFormat) ?: ""
}
