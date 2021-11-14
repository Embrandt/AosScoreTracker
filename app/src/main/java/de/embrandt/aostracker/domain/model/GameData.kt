package de.embrandt.aostracker.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class GameData(
    var battleDate: LocalDate? = null,
    var playerName: String = "",
    var opponentName: String = "",
    var playerFaction: String = "",
    var playerGrandStrategy: String = "",
    var opponentFaction: String = "",
    var opponentGrandStrategy: String = ""
) {
    private val newFormat = DateTimeFormatter.ISO_LOCAL_DATE
    val battleDateText : String
        get() = battleDate?.format(newFormat) ?: ""
}
