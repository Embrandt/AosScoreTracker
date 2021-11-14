package de.embrandt.aostracker.presentation.turn

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.embrandt.aostracker.domain.model.PlayerTurn
import de.embrandt.aostracker.domain.model.Score
import de.embrandt.aostracker.domain.model.TurnData
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme

@Composable
fun TurnScreenStart() {
    val turnViewModel: GameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    TurnScreen(
        turnViewModel.gameData.playerName,
        turnViewModel.gameData.opponentName,
        turnViewModel.currentTurn!!,
        turnViewModel::onTurnDataChanged,
        turnViewModel::onTurnChange,
        turnViewModel::onPlayerScoreChange,
        turnViewModel::onOpponentScoreChange,
        turnViewModel.availablePlayerTactics,
        turnViewModel.availableOpponentTactics
    )
}

@Composable
fun TurnTopBar(turnNumber: Int, onTurnChange: (Int) -> Unit) {
    // TODO  implement dropdown
    Column(
        Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        TopAppBar(
            modifier = Modifier.height(42.dp),
            navigationIcon = {
                IconButton(
                    enabled = turnNumber != 1,
                    onClick = { onTurnChange(turnNumber - 1) },
                    modifier = Modifier.alpha(0.5f)
                ) {
                    Icon(Icons.Outlined.ArrowLeft, contentDescription = null)
                }

            },
            actions = {
                IconButton(enabled = turnNumber != 5, onClick = { onTurnChange(turnNumber + 1) }) {
                    Icon(Icons.Outlined.ArrowRight, contentDescription = null)
                }
            },
            title = {
                TextButton(modifier = Modifier.weight(1f), onClick = { onTurnChange(turnNumber) }) {
                    Text(text = "Turn $turnNumber")
                    Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
private fun TurnScreen(
    myName: String,
    opponentName: String,
    turnInfo: TurnData,
    onTurnDataChange: (TurnData) -> Unit,
    onTurnChange: (Int) -> Unit,
    onPlayerScoreChange: (List<Score>) -> Unit,
    onOpponentScoreChange: (List<Score>) -> Unit,
    availablePlayerTactics: List<String>,
    availableOpponentTactics: List<String>
) {
    Column {
        TurnTopBar(
            turnNumber = turnInfo.turnNumber,
            onTurnChange = { onTurnChange(it) })
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            RollOff(
                playerFirstTurn = turnInfo.playerHasFirstTurn,
                hasFirstTurnChanged = { onTurnDataChange(turnInfo.copy(playerHasFirstTurn = it)) }
            )
            if (turnInfo.playerHasFirstTurn == true) {
                Row {
                    ParticipantTurnColumn(
                        myName = myName,
                        availablePlayerTactics = availablePlayerTactics,
                        turnInfo = turnInfo.playerData,
                        onTurnDataChange = {onTurnDataChange(turnInfo.copy(playerData = it))},
                        onPlayerScoreChange = onPlayerScoreChange,
                        Modifier.weight(1f).padding(end = 8.dp)
                    )
                    ParticipantTurnColumn(
                        myName = opponentName,
                        availablePlayerTactics = availableOpponentTactics,
                        turnInfo = turnInfo.opponentData,
                        onTurnDataChange = {onTurnDataChange(turnInfo.copy(opponentData = it))},
                        onPlayerScoreChange = onOpponentScoreChange,
                        Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            } else {
                Row {
                    ParticipantTurnColumn(
                        myName = opponentName,
                        availablePlayerTactics = availableOpponentTactics,
                        turnInfo = turnInfo.opponentData,
                        onTurnDataChange = {onTurnDataChange(turnInfo.copy(opponentData = it))},
                        onPlayerScoreChange = onOpponentScoreChange,
                        Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    ParticipantTurnColumn(
                        myName = myName,
                        availablePlayerTactics = availablePlayerTactics,
                        turnInfo = turnInfo.playerData,
                        onTurnDataChange = {onTurnDataChange(turnInfo.copy(playerData = it))},
                        onPlayerScoreChange = onPlayerScoreChange,
                        Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ParticipantTurnColumn(
    myName: String,
    availablePlayerTactics: List<String>,
    turnInfo: PlayerTurn,
    onTurnDataChange: (PlayerTurn) -> Unit,
    onPlayerScoreChange: (List<Score>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(text = myName)
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        CommandPointControl()
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        BattleTacticChooser(
            availableTactics = availablePlayerTactics,
            selectedTactic = turnInfo.battleTactic?:"Battle Tactic",
            onTacticChosen = {
                onTurnDataChange(
                    turnInfo.copy(
                        battleTactic = it
                    )
                )
            }
        )
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        PointScoring(
            myName,
            turnInfo.scores,
            { onPlayerScoreChange(it) }
        )
    }
}

@Composable
private fun PointScoring(
    playerName: String,
    scores: List<Score>,
    onScoreChange: (List<Score>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(8.dp)) {
        Text(text = playerName)
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        scores.map { score ->
            ScoringOption(
                scoringOpting = score.scoringOption,
                scored = score.scored,
                onScoredChange = {
                    val newScores = mutableListOf<Score>()
                    for (oldScore in scores) {
                        if (oldScore.scoringOption == score.scoringOption) {
                            newScores.add(oldScore.copy(scored = it))
                        } else {
                            newScores.add(oldScore)
                        }
                    }
                    onScoreChange(newScores)
                }
            )
        }
    }
}

@Composable
fun ScoringOption(scoringOpting: String, scored: Boolean, onScoredChange: (Boolean) -> Unit) {
    Row (Modifier.fillMaxWidth(1f).clickable { onScoredChange(!scored) }){
        Checkbox(checked = scored, onCheckedChange = onScoredChange)
        Text(text = scoringOpting)
    }
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
private fun BattleTacticChooser(
    availableTactics: List<String>,
    selectedTactic: String,
    onTacticChosen: (String) -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    TextField(
        value = selectedTactic,
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconToggleButton(
                checked = checked,
                onCheckedChange = { checked = !checked }
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        },
        label =
        when {
            checked -> {
                {
                    BattleTacticDropdown(
                        availableTactics = availableTactics,
                        expanded = checked,
                        onDismiss = { chosenTactic ->
                            chosenTactic?.apply { onTacticChosen(chosenTactic) }
                            checked = false
                        }
                    )
                }
            }
            else -> null
        }
    )
}

@Composable
fun BattleTacticDropdown(
    availableTactics: List<String>,
    expanded: Boolean,
    onDismiss: (String?) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss(null) }) {
        availableTactics.map {
            DropdownMenuItem(onClick = { onDismiss(it) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(it)
                    // TODO display explanation
                    IconButton(onClick = { Log.i("Pregame", "clicked help") }) {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = "")
                    }
                }
            }
        }
    }
}

@Composable
private fun CommandPointControl() {
    Column {
        Row {
            //TODO spent
            Text("Spent - 0 +")
            // TODO GAINED
            Text("Gained - 0 +")
        }
    }
}

@Preview
@Composable
fun PreviewPointScoring() {
    val scoringOptions = listOf(
        Score("Battle Tactic scored", false), Score("Hold 1", false),
        Score("Hold 2+", false), Score("Hold more", false)
    )
    AosTrackerTheme {
        PointScoring(playerName = "Player", scoringOptions, {})
    }
}

@Preview
@Composable
fun PreviewTurnTopBar() {
    AosTrackerTheme {
        TurnTopBar(turnNumber = 2, onTurnChange = {})
    }
}

@Preview
@Composable
fun PreviewBattleTacticMenu() {
    //TODO make preview work
    Box {
        BattleTacticDropdown(
            availableTactics = listOf("Erst dies", "dann das", "dann jenes"),
            expanded = true,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
fun PreviewBattleTacticChooser() {
    BattleTacticChooser(
        availableTactics = listOf("Tactic A", "Tactic B"),
        selectedTactic = "Chosen Tactic",
        onTacticChosen = {})
}

@Composable
@Preview
private fun TurnScreenPreview() {
    val scoringOptions = listOf(
        Score("Battle Tactic scored", false), Score("Hold 1", false),
        Score("Hold 2+", false), Score("Hold more", false)
    )
    val playerTurn = PlayerTurn(scoringOptions, null)
    val turnData = TurnData(1, playerTurn, playerTurn)
    AosTrackerTheme {
        TurnScreen("Marcel", "Bastl", turnData, {}, {}, {}, {}, listOf("Available"), listOf("Other")
        )
    }
}

@Composable
@Preview
private fun CommandPointsPreview() {
    AosTrackerTheme {
        CommandPointControl()
    }
}