package de.embrandt.aostracker.presentation.pregame

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.presentation.GameViewModel
import de.embrandt.aostracker.ui.theme.AosTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

//@Composable
//fun ExperimentalStuff() {
//    val textView = TextView(requireContext())
//    val spentText = SpannableString("Spent - 0 +")
//    val clickMe: ClickableSpan = object : ClickableSpan() {
//        override fun onClick(widget: View) {
//            Log.i("Pregame", "clicked plus")
//            widget.requestFocus()
//
//        }
//    }
//    spentText.setSpan(clickMe, 10, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    textView.setText(spentText)
//    textView.setMovementMethod(LinkMovementMethod.getInstance())
//    textView.highlightColor = Color.TRANSPARENT
//}


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

//    @OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PregameTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
//        maxLines = 1,
        singleLine = true,
//        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
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
        PregameTextField(
            value = gameData.playerFaction,
            onValueChange = { gameDataChange(gameData.copy(playerFaction = it)) },
            label = "Your Faction"
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
        PregameTextField(
            value = gameData.opponentFaction,
            onValueChange = { gameDataChange(gameData.copy(opponentFaction = it)) },
            label = "Opponents' Faction"
        )
        PregameTextField(
            value = gameData.opponentGrandStrategy,
            onValueChange = { gameDataChange(gameData.copy(opponentGrandStrategy = it)) },
            label = "Opponents' Grand Strategy"
        )
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