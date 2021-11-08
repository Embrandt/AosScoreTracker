package de.embrandt.aostracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.embrandt.aostracker.TurnData
import de.embrandt.aostracker.ui.pregame.PreGameViewModel

class TurnDisplayFragment : Fragment() {

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
                        Row {
                            val viewModels: PreGameViewModel by activityViewModels()
                            TurnScreenFragment(turnViewModel = viewModels)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TurnScreenFragment(turnViewModel: PreGameViewModel) {
        val myName = turnViewModel.gameData.playerName
        val opponentName = turnViewModel.gameData.opponentName
        val currentTurn = turnViewModel.currentTurn!!
        TurnScreen(
            myName = myName,
            opponentName = opponentName,
            currentTurn,
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
        MaterialTheme {
            TurnScreen(myName = "Marcel", opponentName = "Bastl", turnData, {})
        }
    }

    @Composable
    @Preview
    private fun CommandPointsPreview() {
        MaterialTheme() {
            CommandPointControl(userName = "Marcel")
        }
    }
}