package de.embrandt.aostracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import de.embrandt.aostracker.data.data_source.GameDataDao

class GameViewModelFactory(private val dataSource : GameDataDao) : Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}