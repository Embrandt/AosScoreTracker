package de.embrandt.aostracker.presentation.game_selection

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.*


@Composable
fun GameSelectionScreen(existingGames : List<GameData>, onGameClicked : (GameData)-> Unit) {
    Scaffold {
        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            items(existingGames) { game ->
                Column(Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                    Text(text = game.battleDateText, style = MaterialTheme.typography.overline)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onGameClicked(game) }, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${game.playerName} vs. ${game.opponentName}")
                        Text("scoring")
                    }

                }
                Divider()
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun GameSelectionScreen() {
    val gameSelectionViewModel: GameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    val scope = rememberCoroutineScope()
    val allGames = remember { mutableStateListOf<GameData>() }

    scope.launch {
        allGames.addAll(gameSelectionViewModel.getAllGames())
    }
    GameSelectionScreen(existingGames = allGames, onGameClicked = { Log.i("Pregame", "Game clicked ${it.toString()}")})
}
@Preview
@Composable
fun GameSelectionPreview() {
    val listOf = listOf(
        GameData(LocalDate.ofYearDay(2020,12), playerName = "Marcel", opponentName = "Basti"),
        GameData(LocalDate.now(), playerName = "Someone", opponentName = "else")
    )
    AosTrackerTheme {
        GameSelectionScreen(listOf, {})
    }
}