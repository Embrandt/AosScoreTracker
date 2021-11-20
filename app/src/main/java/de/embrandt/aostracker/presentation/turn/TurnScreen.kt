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
import de.embrandt.aostracker.domain.model.*
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme

@Composable
fun TurnScreenStart() {
    val turnViewModel: GameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    turnViewModel.controlCurrentBattleTactic()
    TurnScreen(
        turnViewModel.gameData.playerName,
        turnViewModel.gameData.opponentName,
        turnViewModel.currentTurn!!,
        turnViewModel::onTurnDataChanged,
        turnViewModel::onTurnChange,
        turnViewModel.scoringOptions,
        turnViewModel::onPlayerScoreChange,
        turnViewModel::onOpponentScoreChange,
        turnViewModel.availablePlayerTactics,
        turnViewModel.availableOpponentTactics,
        turnViewModel.gameData.battlePlan
    )
}

@Composable
fun TurnTopBar(turnNumber: Int, onTurnChange: (Int) -> Unit) {
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
                var dropdownOpen by remember { mutableStateOf(false) }
                TextButton(modifier = Modifier.weight(1f), onClick = { dropdownOpen = true }) {
                    Text(text = "Turn $turnNumber")
                    Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = dropdownOpen, onDismissRequest = { dropdownOpen = false }) {
                    for (i in 1..5) {
                        DropdownMenuItem(onClick = {
                            onTurnChange(i)
                            dropdownOpen = false
                        }) {
                            Text("Turn $i")
                        }
                    }
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
    scoringOptions: Set<ScoringOption>,
    onPlayerScoreChange: (Set<ScoringOption>) -> Unit,
    onOpponentScoreChange: (Set<ScoringOption>) -> Unit,
    availablePlayerTactics: List<BattleTactic>,
    availableOpponentTactics: List<BattleTactic>,
    battlePlan: BattlePlan? = null
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
            if (turnInfo.playerHasFirstTurn != null) {
                if (turnInfo.playerHasFirstTurn == true) {
                    Row {
                        ParticipantTurnColumn(
                            battlePlan = battlePlan,
                            scoringOptions= scoringOptions,
                            participantName = myName,
                            availableTactics = availablePlayerTactics,
                            turnInfo = turnInfo.playerData,
                            onTurnDataChange = { onTurnDataChange(turnInfo.copy(playerData = it)) },
                            onPlayerScoreChange = onPlayerScoreChange,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        ParticipantTurnColumn(
                            battlePlan = battlePlan,
                            scoringOptions= scoringOptions,
                            participantName = opponentName,
                            availableTactics = availableOpponentTactics,
                            turnInfo = turnInfo.opponentData,
                            onTurnDataChange = { onTurnDataChange(turnInfo.copy(opponentData = it)) },
                            onPlayerScoreChange = onOpponentScoreChange,
                            modifier = Modifier.weight(1f)

                        )
                    }
                } else {
                    Row {
                        ParticipantTurnColumn(
                            battlePlan = battlePlan,
                            scoringOptions= scoringOptions,
                            participantName = opponentName,
                            availableTactics = availableOpponentTactics,
                            turnInfo = turnInfo.opponentData,
                            onTurnDataChange = { onTurnDataChange(turnInfo.copy(opponentData = it)) },
                            onPlayerScoreChange = onOpponentScoreChange,
                            Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        ParticipantTurnColumn(
                            battlePlan = battlePlan,
                            scoringOptions= scoringOptions,
                            participantName = myName,
                            availableTactics = availablePlayerTactics,
                            turnInfo = turnInfo.playerData,
                            onTurnDataChange = { onTurnDataChange(turnInfo.copy(playerData = it)) },
                            onPlayerScoreChange = onPlayerScoreChange,
                            Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ParticipantTurnColumn(
    battlePlan: BattlePlan?,
    scoringOptions: Set<ScoringOption>,
    participantName: String,
    availableTactics: List<BattleTactic>,
    turnInfo: PlayerTurn,
    onTurnDataChange: (PlayerTurn) -> Unit,
    onPlayerScoreChange: (Set<ScoringOption>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(text = participantName)
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        CommandPointControl(turnInfo, onTurnDataChange)
        BattleTacticChooser(
            availableTactics = availableTactics,
            selectedTactic = turnInfo.battleTactic,
            onTacticChosen = {
                onTurnDataChange(
                    turnInfo.copy(
                        battleTactic = it
                    )
                )
            }
        )
        if (battlePlan != null) {
            PointScoring(
                scoringOptions = scoringOptions,
                scoredByParticipant = turnInfo.scores,
                onScoreChange = { onPlayerScoreChange(it) })
        }
    }
}

@Composable
private fun PointScoring(
    scoringOptions: Set<ScoringOption>,
    scoredByParticipant: Set<ScoringOption>,
    onScoreChange: (Set<ScoringOption>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(top = 16.dp)) {
        Text(text = "Objectives scored", style = MaterialTheme.typography.subtitle2)
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        scoringOptions.map { score ->
            val isScored = score in scoredByParticipant
            ScoringCheckBox(
                scoringOpting = score.shortDescripton,
                scored = isScored,
                onScoredChange = { scoreChanged ->
                    val newScores = scoredByParticipant.toMutableSet()
                    if (scoreChanged) {
                        newScores.add(score)
                    } else {
                        newScores.remove(score)
                    }
                    onScoreChange(newScores)
                }
            )
        }
    }
}

@Composable
fun ScoringCheckBox(scoringOpting: String, scored: Boolean, onScoredChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth(1f)
            .clickable { onScoredChange(!scored) }) {
        Checkbox(checked = scored, onCheckedChange = onScoredChange)
        Text(text = scoringOpting)
    }
}

@Composable
private fun RollOff(playerFirstTurn: Boolean?, hasFirstTurnChanged: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Who has first turn?", style = MaterialTheme.typography.caption)
        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("You", Modifier.padding(8.dp))
            RadioButton(selected = playerFirstTurn == true, onClick = { hasFirstTurnChanged(true) })
            RadioButton(
                selected = playerFirstTurn == false,
                onClick = { hasFirstTurnChanged(false) })
            Text("Opponent", Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun BattleTacticChooser(
    availableTactics: List<BattleTactic>,
    selectedTactic: BattleTactic?,
    onTacticChosen: (BattleTactic) -> Unit
) {
    Column {
        Text(
            "Battle Tactic",
            Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        var checked by remember { mutableStateOf(false) }
        TextField(
            value = selectedTactic?.nameId?:"Battle Tactic",
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
}

@Composable
fun BattleTacticDropdown(
    availableTactics: List<BattleTactic>,
    expanded: Boolean,
    onDismiss: (BattleTactic?) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss(null) }) {
        availableTactics.map {
            DropdownMenuItem(onClick = { onDismiss(it) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(it.nameId)
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
private fun CommandPointControl(turnData: PlayerTurn, onTurnDataChange: (PlayerTurn) -> Unit) {
    Column(Modifier.padding(top = 8.dp)) {
        Text("Command Points", style = MaterialTheme.typography.subtitle2)
        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        Counter(
            label = "Gained",
            number = turnData.commandPointsGained,
            onNumberChange = { if (it >= 0) onTurnDataChange(turnData.copy(commandPointsGained = it)) })
        Counter(
            label = "Spent",
            number = turnData.commandPointsSpent,
            onNumberChange = { if (it >= 0) onTurnDataChange(turnData.copy(commandPointsSpent = it)) })
    }
}

@Composable
fun Counter(label: String, number: Int, onNumberChange: (Int) -> Unit) {
    Text(label, style = MaterialTheme.typography.overline)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = { onNumberChange(number - 1) }) {
            Text("-")
        }
        Text(number.toString())
        TextButton(onClick = { onNumberChange(number + 1) }) {
            Text("+")
        }
    }
}

@Preview
@Composable
fun PreviewPointScoring() {
    AosTrackerTheme {
        PointScoring(BattlePlan.SavageGains.scoringOptions, emptySet(), {})
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
fun PreviewBattleTacticChooser() {

    BattleTacticChooser(
        availableTactics = listOf(BattleTactic.TakeOver),
        selectedTactic = BattleTactic.TakeOver,
        onTacticChosen = {})
}

@Composable
@Preview
private fun TurnScreenRollOfPreview() {
    val playerTurn = PlayerTurn()
    val turnData = TurnData(1, playerTurn, playerTurn)
    AosTrackerTheme {
        TurnScreen("Marcel", "Bastl", turnData, {}, {},
            emptySet(), {}, {}, listOf(BattleTactic.BringItDown), listOf(BattleTactic.BringItDown)
        )
    }
}

@Composable
@Preview
private fun TurnScreenPreview() {
    val playerTurn = PlayerTurn()
    val turnData = TurnData(1, playerTurn, playerTurn, true)
    AosTrackerTheme {
        TurnScreen("Marcel", "Bastl", turnData, {}, {}, emptySet(),{}, {}, listOf(BattleTactic.BringItDown), listOf(BattleTactic.BringItDown)
        )
    }
}

@Composable
@Preview
private fun CommandPointsPreview() {
    val playerTurn = PlayerTurn()
    AosTrackerTheme {
        CommandPointControl(playerTurn, {})
    }
}

@Preview
@Composable
fun RollOfPreview() {
    AosTrackerTheme {
        RollOff(playerFirstTurn = true, hasFirstTurnChanged = {})
    }
}