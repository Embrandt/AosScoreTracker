package de.embrandt.aostracker.ui.pregame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PreGameViewModel : ViewModel() {
    init {
        Log.i("PregameModel", "created")
    }
    val myName = MutableLiveData<String>().apply {
        value = ""
    }

    val myFaction = MutableLiveData<String>()

    private val _battleDate = MutableLiveData<LocalDate>().apply {
        value = LocalDate.now()
    }
    fun setBattleDate(year : Int, month : Int, dayOfMont: Int) {
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
}