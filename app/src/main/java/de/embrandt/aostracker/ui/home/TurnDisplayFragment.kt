package de.embrandt.aostracker.ui.home

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropDownCircle
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
import de.embrandt.aostracker.PlayerTurn
import de.embrandt.aostracker.Score
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
        turnViewModel::onTurnDataChanged,
        turnViewModel::onTurnChange,
        turnViewModel::onPlayerScoreChange,
        turnViewModel::onOpponentScoreChange
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
                    Icon(Icons.Outlined.ArrowDropDownCircle, contentDescription = null)
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
    onOpponentScoreChange: (List<Score>) -> Unit
) {
    Column {
        TurnTopBar(
            turnNumber = turnInfo.turnNumber,
            onTurnChange = { onTurnChange(it) })
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
//                .padding(it)
        ) {

            RollOff(
                playerFirstTurn = turnInfo.playerHasFirstTurn,
                hasFirstTurnChanged = { onTurnDataChange(turnInfo.copy(playerHasFirstTurn = it)) }
            )
            if (turnInfo.playerHasFirstTurn == true) {
                CommandPointControl(myName)
                CommandPointControl(opponentName)
            } else {

                CommandPointControl(opponentName)
                CommandPointControl(myName)
            }
            BattleTacticChooser()
            Row {
                PointScoring(
                    myName,
                    turnInfo.playerData.scores,
                    { onPlayerScoreChange(it) },
                    Modifier.weight(1f)
                )
                PointScoring(
                    opponentName,
                    turnInfo.opponentData.scores,
                    { onOpponentScoreChange(it) },
                    Modifier.weight(1f)
                )
            }
        }
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
    Row {
        Checkbox(checked = scored, onCheckedChange = onScoredChange)
        Text(text = scoringOpting)
    }
}

@Preview
@Composable
fun PreviewPointScoring() {
    val scoringOptions = listOf<Score>(
        Score("Battle Tactic scored", false), Score("Hold 1", false),
        Score("Hold 2+", false), Score("Hold more", false)
    )
    AosTrackerTheme {
        PointScoring(playerName = "Player", scoringOptions, {})
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
private fun BattleTacticChooser() {
    // TODO move to parameter
    val tactics = listOf(
        "Ihre Reihen Zerschmettern",
        "Erobern",
        "Den Kriegsherr töten",
        "Entschlossener Vorstoß",
        "Bringt es zur Strecke",
        "Aggresive Expansion",
        "Monströse Übernahme",
        "Wilde Speerspitze"
    )
    var checked by remember { mutableStateOf(false) }
    // TODO move to turndata
    var selectedTactic by remember {
        mutableStateOf("BattleTactic")
    }
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
                        availableTactics = tactics,
                        expanded = checked,
                        onDismiss = { chosenTactic ->
                            selectedTactic = chosenTactic
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
    onDismiss: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss("nix") }) {
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
    BattleTacticChooser()
}

@Composable
@Preview
private fun TurnScreenPreview() {
    val scoringOptions = listOf<Score>(
        Score("Battle Tactic scored", false), Score("Hold 1", false),
        Score("Hold 2+", false), Score("Hold more", false)
    )
    val playerTurn = PlayerTurn(scoringOptions, null)
    val turnData = TurnData(1, playerTurn, playerTurn)
    AosTrackerTheme {
        TurnScreen(myName = "Marcel", opponentName = "Bastl", turnData, {}, {}, {}, {})
    }
}

@Composable
@Preview
private fun CommandPointsPreview() {
    AosTrackerTheme {
        CommandPointControl(userName = "Marcel")
    }
}