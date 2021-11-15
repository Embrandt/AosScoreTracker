package de.embrandt.aostracker.presentation.pregame

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import de.embrandt.aostracker.domain.model.Faction
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.domain.util.Configuration
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private fun selectDate(context: AppCompatActivity, updateDate: (LocalDate) -> Unit) {
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
    PregameContent(gameData = viewModel.gameData, gameDataChange = viewModel::onGameDataChanged)
}

@Composable
fun PregameContent(gameData: GameData, gameDataChange: (GameData) -> Unit) {
    val activity = LocalContext.current as AppCompatActivity


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
                            updateDate = { gameDataChange(gameData.copy(battleDate = it)) }
                        )
                    }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "")
                }
            })
        PregameTextField(
            value = gameData.playerName,
            onValueChange = { gameDataChange(gameData.copy(playerName = it)) },
            label = "Your Name"
        )

        FactionFieldWithChoiceDialog(
            defaultLabel = "Your Faction",
            selectedFaction = gameData.playerFaction,
            onFactionSelected = { gameDataChange(gameData.copy(playerFaction = it)) }
        )

        PregameTextField(
            value = gameData.playerGrandStrategy,
            onValueChange = { gameDataChange(gameData.copy(playerGrandStrategy = it)) },
            label = "Your Grand Strategy"
        )
        PregameTextField(
            value = gameData.opponentName,
            onValueChange = { gameDataChange(gameData.copy(opponentName = it)) },
            label = "Opponents' Name"
        )

        FactionFieldWithChoiceDialog(
            defaultLabel = "Opponents' Faction",
            selectedFaction = gameData.opponentFaction,
            onFactionSelected = { gameDataChange(gameData.copy(opponentFaction = it)) }
        )

        PregameTextField(
            value = gameData.opponentGrandStrategy,
            onValueChange = { gameDataChange(gameData.copy(opponentGrandStrategy = it)) },
            label = "Opponents' Grand Strategy"
        )
    }
}

@Composable
fun FactionFieldWithChoiceDialog(
    defaultLabel: String,
    selectedFaction: Faction?,
    onFactionSelected: (Faction) -> Unit
) {
    var openFactionDialog by remember { mutableStateOf(false) }
    TextField(
        value = selectedFaction?.name ?: defaultLabel,
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
            text = {
                Column(Modifier.selectableGroup()) {
                    Configuration.factions.map { faction ->
                        FactionRadioButton(
                            faction = faction,
                            selected = selectedFaction == faction,
                            onFactionSelected = { onFactionSelected(it) }
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
        Text(text = faction.name)
    }
}

@Preview
@Composable
private fun PreViewScreen() {
    val gameData = GameData(null, playerName = "Marcel")
    AosTrackerTheme {
        PregameContent(gameData, gameDataChange = {})
    }
}