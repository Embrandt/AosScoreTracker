package de.embrandt.aostracker.domain.util

import androidx.room.TypeConverter
import de.embrandt.aostracker.domain.model.ScoringOption
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