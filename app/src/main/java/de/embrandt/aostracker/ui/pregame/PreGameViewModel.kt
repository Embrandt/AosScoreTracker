package de.embrandt.aostracker.ui.pregame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.embrandt.aostracker.TurnData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    val myName = MutableLiveData<String>().apply {
        value = ""
    }

    val myFaction = MutableLiveData<String>()

    private val _battleDate = MutableLiveData<LocalDate>().apply {
        value = LocalDate.now()
    }

    fun setBattleDate(year: Int, month: Int, dayOfMont: Int) {
        _battleDate.value = LocalDate.of(year, month, dayOfMont)
    }

    val battleDateText: LiveData<String> = Transformations.map(_battleDate) {
        val newFormat = DateTimeFormatter.ISO_LOCAL_DATE
        it.format(newFormat)
    }

    val myGrandStrategy = MutableLiveData<String>()
    val opponentFaction = MutableLiveData<String>()
    val opponentName = MutableLiveData<String>()
    val opponentGrandStrategy = MutableLiveData<String>()


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