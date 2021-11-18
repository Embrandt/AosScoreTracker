package de.embrandt.aostracker.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
data class GameData(
    val battleDate: LocalDate?,
    val battlePack: BattlePack? = BattlePack.Gur,
    val battlePlan: BattlePlan? = null,
    val playerName: String = "",
    val opponentName: String = "",
    val playerFaction: Faction? = null,
    val playerGrandStrategy: String = "",
    val opponentFaction: Faction? = null,
    val opponentGrandStrategy: String = "",
    @PrimaryKey(autoGenerate = true)
    val gameId : Long = 0L
) {
    @Ignore
    private val newFormat = DateTimeFormatter.ISO_LOCAL_DATE

    val battleDateText : String
        @Ignore
        get() = battleDate?.format(newFormat) ?: ""
}
