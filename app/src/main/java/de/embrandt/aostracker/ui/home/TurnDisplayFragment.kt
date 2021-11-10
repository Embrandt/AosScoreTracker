package de.embrandt.aostracker.ui.home

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.embrandt.aostracker.TurnData
import de.embrandt.aostracker.ui.pregame.PreGameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme

@Composable
fun TurnScreenStart() {
    val turnViewModel: PreGameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    TurnScreen(
        turnViewModel.gameData.playerName,
        turnViewModel.gameData.opponentName,
        turnViewModel.currentTurn!!,
        turnViewModel::onTurnDataChanged
    )
}


@Composable
private fun TurnScreen(
    myName: String,
    opponentName: String,
    turnInfo: TurnData,
    onTurnDataChange: (TurnData) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = turnInfo.turnNumber.toString(),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            readOnly = true
        )
        RollOff(
            playerFirstTurn = turnInfo.playerHasFirstTurn,
            hasFirstTurnChanged = { onTurnDataChange(turnInfo.copy(playerHasFirstTurn = it)) }
        )
        CommandPointControl(myName)
        CommandPointControl(opponentName)
    }
}

@Composable
private fun PointScoring() {
    // TODO implement scoring checkboxes
}

@Composable
private fun RollOff(playerFirstTurn: Boolean?, hasFirstTurnChanged: (Boolean) -> Unit) {
    if (playerFirstTurn == null) {
        // TODO make initial choice
        Text("shit happened")
    } else {
        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(selected = playerFirstTurn, onClick = { hasFirstTurnChanged(true) })
            Text("Me", Modifier.padding(8.dp))
            RadioButton(selected = !playerFirstTurn, onClick = { hasFirstTurnChanged(false) })
            Text("You", Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun BattleTacticChooser() {
    //TODO implement battle tactic to choose
}

@Composable
private fun CommandPointControl(userName: String) {
    Column {
        Text(userName)
        Row {
            //TODO spent
            Text("Spent - 0 +")
            // TODO GAINED
            Text("Gained - 0 +")
        }
    }
}

@Composable
@Preview
private fun TurnScreenPreview() {
    val turnData = TurnData(1, true)
    AosTrackerTheme {
        TurnScreen(myName = "Marcel", opponentName = "Bastl", turnData, {})
    }
}

@Composable
@Preview
private fun CommandPointsPreview() {
    AosTrackerTheme {
        CommandPointControl(userName = "Marcel")
    }
}