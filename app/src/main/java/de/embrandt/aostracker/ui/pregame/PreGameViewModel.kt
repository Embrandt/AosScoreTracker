package de.embrandt.aostracker.ui.pregame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.embrandt.aostracker.GameData
import de.embrandt.aostracker.TurnData
import java.time.LocalDate

class PreGameViewModel : ViewModel() {
    var turnStats = mutableStateListOf<TurnData>()
        private set

    private fun initializeTurns() {
        for (i in 1..5) {
            turnStats.add(TurnData(i))
        }
    }

    init {
        Log.i("PregameModel", "created")
        initializeTurns()
    }

    var gameData by mutableStateOf<GameData>(GameData(LocalDate.now()))
    fun onGameDataChanged(newData : GameData) {
        gameData = newData
    }
    fun setBattleDate(year: Int, month: Int, dayOfMont: Int) {
        onGameDataChanged(gameData.copy(battleDate = LocalDate.of(year, month, dayOfMont)))
    }

    private var currentTurnNumber by mutableStateOf(0)

    val currentTurn: TurnData?
        get() = turnStats.getOrNull(currentTurnNumber)

    fun onTurnDataChanged(turnData: TurnData) {
        require(currentTurn?.turnNumber == turnData.turnNumber) {
            "You can only change data of the current turn"
        }
        turnStats[currentTurnNumber] = turnData
    }


}