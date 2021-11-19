package de.embrandt.aostracker.data.data_source

import androidx.room.*
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.domain.model.GameDataWithTurnScoring
import de.embrandt.aostracker.domain.model.TurnData

@Dao
interface GameDataDao {
    @Insert
    suspend fun insert(gameData: GameData)

    @Update
    suspend fun update(gameData: GameData)

    @Query("SELECT * FROM gamedata ORDER BY gameId ASC LIMIT 1")
    suspend fun getCurrentGame(): GameData?

    @Transaction
    @Query("SELECT * FROM gamedata ORDER BY gameId ASC LIMIT 1")
    suspend fun getCurrentGameWithScoring(): GameDataWithTurnScoring?

    @Insert
    suspend fun insert(turnData: TurnData)

    @Insert
    suspend fun insertList(turnDatas : List<TurnData>)

    @Update
    suspend fun update(turnData: TurnData)
}