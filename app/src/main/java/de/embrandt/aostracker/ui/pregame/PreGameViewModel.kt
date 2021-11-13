package de.embrandt.aostracker.ui.pregame

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import de.embrandt.aostracker.GameData
import de.embrandt.aostracker.PlayerTurn
import de.embrandt.aostracker.Score
import de.embrandt.aostracker.TurnData
import java.time.LocalDate

class PreGameViewModel : ViewModel() {
    var turnStats = mutableStateListOf<TurnData>()
        private set

    // TODO initialize from mission
    private val scoringOptionsPlayer = listOf(
        Score("Battle Tactic scored", false), Score("Hold 1", false),
        Score("Hold 2+", false), Score("Hold more", false)
    )
    private val tactics = listOf(
        "Ihre Reihen Zerschmettern",
        "Erobern",
        "Den Kriegsherr töten",
        "Entschlossener Vorstoß",
        "Bringt es zur Strecke",
        "Aggresive Expansion",
        "Monströse Übernahme",
        "Wilde Speerspitze"
    )

    private fun initializeTurns() {
        for (i in 1..5) {

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

    val availablePlayerTactics: List<String> by derivedStateOf {
        val available = tactics.toMutableList()
        for (i in 0 until currentTurnNumber) {
            available.remove(turnStats[i].playerData.battleTactic)
        }
        return@derivedStateOf available
    }

    val availableOpponentTactics: List<String> by derivedStateOf {
        val available = tactics.toMutableList()
        for (turnData in turnStats) {
            available.remove(turnData.opponentData.battleTactic)
        }
        return@derivedStateOf available
    }

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