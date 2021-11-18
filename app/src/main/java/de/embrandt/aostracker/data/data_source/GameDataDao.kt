package de.embrandt.aostracker.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.embrandt.aostracker.domain.model.GameData

@Dao
interface GameDataDao {
    @Insert
    suspend fun insert(gameData : GameData)

    @Update
    suspend fun update(gameData: GameData)

    @Query("SELECT * FROM gamedata ORDER BY gameId ASC LIMIT 1")
    suspend fun getCurrentGame() : GameData?
}