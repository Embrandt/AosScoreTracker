package de.embrandt.aostracker.ui.notifications

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.embrandt.aostracker.GameData
import de.embrandt.aostracker.ui.pregame.DatePickerFragment
import de.embrandt.aostracker.ui.pregame.PreGameViewModel

class PureComposePregameFragment : Fragment() {

    private val notificationsViewModel: PreGameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                MaterialTheme {
                    Surface {
                        PregameScreen(notificationsViewModel.gameData, { notificationsViewModel.onGameDataChanged(it) })
                        // TODO fill with pregame stuff
                    }
                }
            }
        }
    }

    @Composable
    fun ExperimentalStuff() {
        val textView = TextView(requireContext())
        val spentText = SpannableString("Spent - 0 +")
        val clickMe: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.i("Pregame", "clicked plus")
                widget.requestFocus()

            }
        }
        spentText.setSpan(clickMe, 10, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(spentText)
        textView.setMovementMethod(LinkMovementMethod.getInstance())
        textView.highlightColor = Color.TRANSPARENT
    }


    //    @android:drawable/ic_menu_my_calendar
    private fun selectDate() {
        Log.i("PregameFragment", "open dialog here")
        val newFragment = DatePickerFragment()
        newFragment.show(parentFragmentManager, "datepicker")
    }

//    @OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PregameTextField(value : String, onValueChange : (String) -> Unit, label : String) {
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
    fun PregameScreen(gamedata : GameData, gameDataChange : (GameData) -> Unit) {
        Column (
            Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
//                Log.i("Tag", this.)
                TextField(value = gamedata.battleDateText,
                    onValueChange = {},
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    label = { Text("BattleDate") },
                    trailingIcon = {
                        IconButton(onClick = { selectDate() }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "")
                        }
                    })
            PregameTextField(
                value = gamedata.playerName,
                onValueChange = { gameDataChange(gamedata.copy(playerName = it)) },
                label = "Your Name"
            )
            PregameTextField(
                    value = gamedata.playerFaction,
                    onValueChange = { gameDataChange(gamedata.copy(playerFaction = it)) },
                    label = "Your Faction"
            )
            PregameTextField(
                value = gamedata.playerGrandStrategy,
                onValueChange = { gameDataChange(gamedata.copy(playerGrandStrategy = it)) },
                label = "Your Grand Strategy"
            )
            PregameTextField(
                value = gamedata.opponentName,
                onValueChange = {gameDataChange(gamedata.copy(opponentName = it)) },
                label = "Opponents' Name"
            )
            PregameTextField(
                value = gamedata.opponentFaction,
                onValueChange = {gameDataChange(gamedata.copy(opponentFaction = it)) },
                label = "Opponents' Faction"
            )
            PregameTextField(
                value = gamedata.opponentGrandStrategy,
                onValueChange = {gameDataChange(gamedata.copy(opponentGrandStrategy = it)) },
                label = "Opponents' Grand Strategy"
            )
        }
    }
    @Preview
    @Composable
    private fun PreViewScreen() {
        val gameData = GameData(null, playerName = "Marcel")
        PregameScreen(gameData, gameDataChange = {})
    }
}