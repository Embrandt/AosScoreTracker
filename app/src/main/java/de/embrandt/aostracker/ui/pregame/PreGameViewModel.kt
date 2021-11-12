package de.embrandt.aostracker.ui.pregame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.embrandt.aostracker.GameData
import de.embrandt.aostracker.PlayerTurn
import de.embrandt.aostracker.Score
import de.embrandt.aostracker.TurnData
import java.time.LocalDate

class PreGameViewModel : ViewModel() {
    var turnStats = mutableStateListOf<TurnData>()
        private set

    private fun initializeTurns() {
        for (i in 1..5) {
            // TODO initialize from mission
            val scoringOptionsPlayer = listOf(
                Score("Battle Tactic scored", false), Score("Hold 1", false),
                Score("Hold 2+", false), Score("Hold more", false)
            )
            val playerTurn = PlayerTurn(scores = scoringOptionsPlayer, null)
            val opponentTurn = PlayerTurn(scores = scoringOptionsPlayer, null)
            val turnData = TurnData(i, playerTurn, opponentTurn)
            turnStats.add(turnData)
        }
    }

    init {
        Log.i("PregameModel", "created")
        initializeTurns()
    }

    var gameData by mutableStateOf<GameData>(GameData(LocalDate.now()))
    fun onGameDataChanged(newData: GameData) {
        gameData = newData
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

    fun onTurnChange(turnNumber: Int) {
        currentTurnNumber = turnNumber - 1
    }

    fun onPlayerScoreChange(newScores: List<Score>) {
        currentTurn?.let {
            val changedTurnData = it.copy(playerData = it.playerData.copy(scores = newScores))
            onTurnDataChanged(changedTurnData)
        }
    }

    fun onOpponentScoreChange(newScores: List<Score>) {
        currentTurn?.let {
            val changedTurnData = it.copy(opponentData = it.opponentData.copy(scores = newScores))
            onTurnDataChanged(changedTurnData)
        }
    }

}