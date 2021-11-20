package de.embrandt.aostracker.presentation

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.embrandt.aostracker.data.data_source.GameDataDao
import de.embrandt.aostracker.domain.model.*
import de.embrandt.aostracker.domain.use_case.GetAvailableTactics
import kotlinx.coroutines.launch
import java.time.LocalDate

class GameViewModel(dataSource: GameDataDao) : ViewModel() {
    private var turnStats = mutableStateListOf<TurnData>()
    private val getAvailableTactics = GetAvailableTactics()
    private val database = dataSource
    var gameData by mutableStateOf(GameData(battleDate = LocalDate.now()))

    init {
        Log.i("PregameModel", "created")
        initGameData()
    }

    private suspend fun insertTurnData(data: List<TurnData>) {
        Log.i("Pregame", "Insert turndata with gameId ${data[0].gameId}")
        database.insertList(data)
    }

    private suspend fun updateTurnData(data: TurnData) {
        database.update(data)
    }

    private fun initGameData() {
        viewModelScope.launch {
            val myStuff = getCurrentGameFromDatabase()
            myStuff?.let { gameData = it }
            Log.i("Pregame", "got data back")
            if (turnStats.isEmpty()) {
                for (i in 1..5) {
                    Log.i("Pregame", "turnstats empty")
                    val playerTurn = PlayerTurn()
                    val opponentTurn = PlayerTurn()
                    val turnData = TurnData(i, playerTurn, opponentTurn, gameId = gameData.gameId)
                    turnStats.add(turnData)
                }
                insertTurnData(turnStats)
            }
            val allMyStugg = database.getAllGames()
            Log.i("Pregame", "got all games ${allMyStugg.size}")
        }
    }

    private suspend fun getCurrentGameFromDatabase(): GameData? {
        val currentGameWithScoring = database.getCurrentGameWithScoring()
        var currentGame = currentGameWithScoring?.gameData
        if (currentGame == null) {
            database.insert(GameData(battleDate = LocalDate.now()))
            currentGame = database.getCurrentGame()
        } else {
            Log.i("Pregame", "get old stats")
            currentGameWithScoring?.let {
                Log.i("Pregame", "turn 1 is ${it.turns.size}")
                turnStats.addAll(it.turns)
            }
        }
        return currentGame

    }

    private suspend fun updateGameData() {
        database.update(gameData)
    }

    fun onGameDataChanged(newData: GameData) {
        gameData = newData
        viewModelScope.launch { updateGameData() }
    }

    fun onBattlePlanChanged(battlePlan: BattlePlan) {
        val newStats = mutableListOf<TurnData>()
        for (turnData in turnStats) {
            newStats.add(turnData.copy(playerData = turnData.playerData.copy(scores = emptySet())))
        }
        turnStats.clear()
        turnStats.addAll(newStats)
        onGameDataChanged(gameData.copy(battlePlan = battlePlan))
    }

    private var currentTurnNumber by mutableStateOf(0)

    val currentTurn: TurnData?
        get() = turnStats.getOrNull(currentTurnNumber)

    val availablePlayerTactics: List<BattleTactic> by derivedStateOf {
        val faction = gameData.playerFaction
        val battlePack = gameData.battlePack

        val available = getAvailableTactics(faction, battlePack).toMutableList()

        for (i in 0 until currentTurnNumber) {
            available.removeIf { it == turnStats[i].playerData.battleTactic }
        }

        return@derivedStateOf available
    }
    val scoringOptions: Set<ScoringOption> by derivedStateOf {
        val scoringOptions = mutableSetOf<ScoringOption>()
        gameData.battlePack?.let { scoringOptions.addAll(it.scoringOptions) }
        gameData.battlePlan?.let { scoringOptions.addAll(it.scoringOptions) }
        return@derivedStateOf scoringOptions
    }

    val availableOpponentTactics: List<BattleTactic> by derivedStateOf {
        val faction = gameData.opponentFaction
        val battlePack = gameData.battlePack

        val available = getAvailableTactics(faction, battlePack).toMutableList()

        for (i in 0 until currentTurnNumber) {
            available.removeIf { it == turnStats[i].opponentData.battleTactic }
        }
        return@derivedStateOf available
    }

    val playerTotalScore: Int by derivedStateOf {
        var totalScore = 0
        for (turnStat in turnStats) {
            for (score in turnStat.playerData.scores) {
                totalScore += score.pointValue
            }
        }
        totalScore
    }
    val opponentTotalScore: Int by derivedStateOf {
        var totalScore = 0
        for (turnStat in turnStats) {
            for (score in turnStat.opponentData.scores) {
                totalScore += score.pointValue
            }
        }
        totalScore
    }

    fun onTurnDataChanged(turnData: TurnData) {
        require(currentTurn?.turnNumber == turnData.turnNumber) {
            "You can only change data of the current turn"
        }
        turnStats[currentTurnNumber] = turnData
        viewModelScope.launch {
            Log.i("Pregame", "starting to update turndata")
            updateTurnData(turnData)
            Log.i("Pregame", "turndata updated")
        }
    }

    fun onTurnChange(turnNumber: Int) {
        currentTurnNumber = turnNumber - 1
    }

    fun onPlayerScoreChange(newScores: Set<ScoringOption>) {
        currentTurn?.let {
            val changedTurnData = it.copy(playerData = it.playerData.copy(scores = newScores))
            onTurnDataChanged(changedTurnData)
        }
    }

    fun onOpponentScoreChange(newScores: Set<ScoringOption>) {
        currentTurn?.let {
            val changedTurnData =
                it.copy(opponentData = it.opponentData.copy(scores = newScores))
            onTurnDataChanged(changedTurnData)
        }
    }

    fun controlCurrentBattleTactic() {
        val thisTurn = currentTurn
        requireNotNull(thisTurn)
        val playerData = thisTurn.playerData
        val opponentData = thisTurn.opponentData
        if (playerData.battleTactic != null && playerData.battleTactic !in availablePlayerTactics) {
            Log.i("Pregame", "caught wrong playerdata")
            onTurnDataChanged(thisTurn.copy(playerData = playerData.copy(battleTactic = null)))
        }
        if (opponentData.battleTactic != null && opponentData.battleTactic !in availableOpponentTactics) {
            Log.i("Pregame", "caught wrong opponentData")
            onTurnDataChanged(thisTurn.copy(opponentData = opponentData.copy(battleTactic = null)))
        }
    }

    // TODO move to own viewModel
    suspend fun getAllGames(): List<GameData> {
        return database.getAllGames()
    }

}