package de.embrandt.aostracker.presentation.pregame

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import de.embrandt.aostracker.domain.model.BattlePlan
import de.embrandt.aostracker.domain.model.Faction
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.domain.model.GrandStrategy
import de.embrandt.aostracker.domain.util.Configuration
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private fun selectDate(context: Context, updateDate: (LocalDate) -> Unit) {
    context as AppCompatActivity
    val picker = MaterialDatePicker.Builder.datePicker().build()
    picker.show(context.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        val instance = Calendar.getInstance()
        instance.timeInMillis = it
        val date: LocalDate =
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        updateDate(date)
    }
}

@Composable
private fun PregameTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        label = { Text(label) })
}

@Composable
fun PregameScreen() {
    val viewModel: GameViewModel = viewModel(LocalContext.current as AppCompatActivity)
    PregameContent(
        gameData = viewModel.gameData,
        viewModel.availablePlayerGrandStrategies,
        viewModel.availableOpponentGrandStrategies,
        onGameDataChange = viewModel::onGameDataChanged,
        onBattlePlanChange = viewModel::onBattlePlanChanged
    )
}

@Composable
fun PregameContent(
    gameData: GameData,
    playerAvailableGrandStrategies: List<GrandStrategy>,
    opponentAvailableGrandStrategies: List<GrandStrategy>,
    onGameDataChange: (GameData) -> Unit,
    onBattlePlanChange: (BattlePlan) -> Unit
) {
    val activity = LocalContext.current


    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(value = gameData.battleDateText,
            onValueChange = {},
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = { Text("BattleDate") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        selectDate(
                            context = activity,
                            updateDate = { onGameDataChange(gameData.copy(battleDate = it)) }
                        )
                    }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "")
                }
            })
        Box {
            var dropdownOpen by remember { mutableStateOf(false) }
            TextField(
                value = gameData.battlePlan?.let { stringResource(id = it.nameRessource) } ?: "",
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = { Text("BattlePlan") },
                trailingIcon = {
                    IconButton(
                        onClick = { dropdownOpen = true }
                    ) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                    }
                })
            DropdownMenu(
                expanded = dropdownOpen,
                onDismissRequest = { dropdownOpen = false }) {
                Configuration.battlePlans.map {
                    DropdownMenuItem(
                        onClick = {
                            onBattlePlanChange(it)
                            dropdownOpen = false
                        }) {
                        Text(stringResource(id = it.nameRessource))
                    }
                }
            }
        }
        PregameTextField(
            value = gameData.playerName,
            onValueChange = { onGameDataChange(gameData.copy(playerName = it)) },
            label = "Your Name"
        )

        FactionFieldWithChoiceDialog(
            defaultLabel = "Your Faction",
            selectedFaction = gameData.playerFaction,
            onFactionSelected = { onGameDataChange(gameData.copy(playerFaction = it)) }
        )

        StrategyFieldWithDropDown(
            label = "Your Grand Strategy",
            selectedStrategy = gameData.playerGrandStrategy,
            availableGrandStrategies = playerAvailableGrandStrategies,
            onStrategySelected = { onGameDataChange(gameData.copy(playerGrandStrategy = it)) }
        )

        PregameTextField(
            value = gameData.opponentName,
            onValueChange = { onGameDataChange(gameData.copy(opponentName = it)) },
            label = "Opponents' Name"
        )

        FactionFieldWithChoiceDialog(
            defaultLabel = "Opponents' Faction",
            selectedFaction = gameData.opponentFaction,
            onFactionSelected = { onGameDataChange(gameData.copy(opponentFaction = it)) }
        )

        StrategyFieldWithDropDown(
            label = "Opponents' Grand Strategy",
            selectedStrategy = gameData.opponentGrandStrategy,
            availableGrandStrategies = opponentAvailableGrandStrategies,
            onStrategySelected = { onGameDataChange(gameData.copy(opponentGrandStrategy = it)) }
        )
    }
}

@Composable
private fun StrategyFieldWithDropDown(
    label: String,
    selectedStrategy: GrandStrategy?,
    availableGrandStrategies: List<GrandStrategy>,
    onStrategySelected: (GrandStrategy) -> Unit
) {
    Box {
        var dropdownOpen by remember { mutableStateOf(false) }
        TextField(
            value = selectedStrategy?.let { stringResource(id = it.nameResource) }
                ?: "",
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = { Text(label) },
            trailingIcon = {
                IconButton(
                    onClick = { dropdownOpen = true }
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }
            })
        DropdownMenu(
            expanded = dropdownOpen,
            onDismissRequest = { dropdownOpen = false }) {
            availableGrandStrategies.map {
                DropdownMenuItem(
                    onClick = {
                        onStrategySelected(it)
                        dropdownOpen = false
                    }) {
                    Text(stringResource(id = it.nameResource))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FactionFieldWithChoiceDialog(
    defaultLabel: String,
    selectedFaction: Faction?,
    onFactionSelected: (Faction) -> Unit
) {
    var openFactionDialog by remember { mutableStateOf(false) }
    TextField(
        value = selectedFaction?.let { stringResource(id = it.resourceId) } ?: defaultLabel,
        onValueChange = { },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        label = { Text(defaultLabel) },
        trailingIcon = {
            IconButton(
                onClick = {
                    openFactionDialog = true
                }
            ) {
                Icon(Icons.Default.Edit, contentDescription = "")
            }
        })
    if (openFactionDialog) {
        AlertDialog(
            onDismissRequest = { openFactionDialog = false },
            title = {
                Text(text = "Select Faction")
            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
            text = {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    items(Configuration.factions) { faction ->
                        FactionRadioButton(
                            faction = faction,
                            selected = selectedFaction == faction,
                            onFactionSelected = {
                                onFactionSelected(it)
                                openFactionDialog = false
                            }
                        )
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openFactionDialog = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}

@Composable
fun FactionRadioButton(faction: Faction, selected: Boolean, onFactionSelected: (Faction) -> Unit) {
    Row(
        Modifier
            .padding(8.dp)
            .selectable(
                selected = selected,
                onClick = { onFactionSelected(faction) },
                role = Role.RadioButton
            )
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text = stringResource(faction.resourceId), modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview
@Composable
private fun PreViewScreen() {
    val gameData = GameData(null, playerName = "Marcel")
    AosTrackerTheme {
        PregameContent(gameData, emptyList(), emptyList(), onGameDataChange = {}, {})
    }
}