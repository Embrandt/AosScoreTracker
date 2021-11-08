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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
                        PregameScreen()
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

    @Preview
    @Composable
    fun PregameScreen() {
        Column {
            TextField(value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("BattleDate") },
                trailingIcon = {
                    IconButton(onClick = { selectDate() }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "")
                    }
                })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Your Name") })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Your Faction") })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Your Grand Strategy") })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Opponents' Name") })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Opponents' Faction") })
            TextField(
                value = "",
                onValueChange = {},
                Modifier.padding(8.dp),
                label = { Text("Opponents' Grand Strategy") })
        }
    }
}