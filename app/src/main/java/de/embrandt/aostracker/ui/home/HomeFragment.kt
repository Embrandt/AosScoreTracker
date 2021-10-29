package de.embrandt.aostracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import de.embrandt.aostracker.databinding.FragmentHomeBinding
import de.embrandt.aostracker.ui.pregame.PreGameViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.composeView.setContent {
            MaterialTheme {
            Surface {
                Row {
                    Column(Modifier.weight(1f)) {
                        homeBinding(viewModel = homeViewModel)
                    }
                    val viewModels: PreGameViewModel by activityViewModels()
                    TurnScreenFragment(turnViewModel = viewModels)
                }
            }
            }
        }
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun stuffInput() {
        val keyBoardController = LocalSoftwareKeyboardController.current
        val (text, setText) = remember { mutableStateOf("")}
        TextField(
            value = text,
            onValueChange = { setText(it) },
            readOnly = false,
            label = { Text("string") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                Log.i("Pregame", "i am pressed")
                keyBoardController?.hide()
            }, onSend = {keyBoardController?.hide()}),
            singleLine = true,
            maxLines = 1)

    }

    @Preview(showBackground = true, name = "Home preview", widthDp = 320)
    @Composable
    private fun DefaultPreview() {
        MaterialTheme {
            Row {
                Column(Modifier.weight(1f)) {
                    homeTextView(text = "Heimat")
                }
                stuffInput()
            }
        }
    }
    @Composable
    private fun homeTextView(text : String) {
        Text(text, Modifier.padding(8.dp))
    }
    @Composable
    private fun homeBinding(viewModel : HomeViewModel) {
        val homeText by viewModel.text.observeAsState()
        homeText?.let { homeTextView(text = it) }
    }


    @Composable
    private fun TurnScreenFragment(turnViewModel : PreGameViewModel) {
        val myName by turnViewModel.myName.observeAsState("")
        val opponentName by turnViewModel.opponentName.observeAsState("")
        TurnScreen(myName = myName, opponentName = opponentName, turnViewModel.currentTurn!!)

    }


    @Composable
    private fun TurnScreen(myName : String, opponentName : String, turnInfo: String) {
        Column() {
            TextField(value = turnInfo, onValueChange = {}, readOnly = true)
            CommandPointControl(myName)
            CommandPointControl(opponentName)
        }
    }

    @Composable
    private fun PointScoring() {
        // TODO implement scoring checkboxes
    }
    @Composable
    private fun RollOff() {
        // TODO implement choosing who had first turn
    }

    @Composable
    private fun BattleTacticChooser() {
        //TODO implement battle tactic to choose
    }
    @Composable
    @Preview
    private fun TurnScreenPreview() {
        MaterialTheme {
            TurnScreen(myName = "Marcel", opponentName = "Bastl", turnInfo = "1")
        }
    }
    
    @Composable
    @Preview(showBackground = true)
    private fun CommandPointsPreview() {
        MaterialTheme() {
            CommandPointControl(userName = "Marcel")
        }
    }

    @Composable
    private fun CommandPointControl(userName : String) {
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}