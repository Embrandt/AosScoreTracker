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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import de.embrandt.aostracker.databinding.FragmentNotificationsBinding
import de.embrandt.aostracker.ui.pregame.PreGameViewModel

class NotificationsFragment : Fragment() {

    private val notificationsViewModel: PreGameViewModel by activityViewModels()
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        val spentText = SpannableString("Spent - 0 +")
        val clickMe : ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.i("Pregame", "clicked plus")
                widget.requestFocus()

            }
        }
        spentText.setSpan(clickMe, 10, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView4.setText(spentText)
        binding.textView4.setMovementMethod(LinkMovementMethod.getInstance())
        binding.textView4.highlightColor = Color.TRANSPARENT

//        binding.spent1.editText?.
////        binding.spent1.editText?.setText(spentText)
//        binding.spent1.editText?.setMovementMethod(LinkMovementMethod.getInstance())
//        binding.spent1.editText?.highlightColor = Color.TRANSPARENT
        binding.composeView.setContent {
            MaterialTheme {
                CommandPointView(notificationsViewModel)
                linkStuff()
            }
        }
        notificationsViewModel.myName.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    @Composable
    fun CommandPointView (viewModel : PreGameViewModel) {
        val myName by viewModel.myName.observeAsState()
        myName?.let { CommandPointContent(it) }
    }

    @Composable
    fun CommandPointContent(commandText : String) {
        Text(commandText)
    }
    @Composable
    fun linkStuff() {
        val spentText = SpannableString("Spent - 0 +")
        val clickMe : ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.i("Pregame", "clicked plus")
                widget.requestFocus()

            }
        }
        spentText.setSpan(clickMe, 10, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val parent :ViewGroup = binding.textView4.parent as ViewGroup
        parent.removeView(binding.textView4)
        binding.textView4.setText(spentText)
        binding.textView4.setMovementMethod(LinkMovementMethod.getInstance())
        binding.textView4.highlightColor = Color.TRANSPARENT
        AndroidView(factory = { binding.textView4}, update = {it.setText(spentText)})
    }
    @Preview
    @Composable
    private fun CommandPointPreview () {
        MaterialTheme {
            CommandPointContent("Spent stuff")
            
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}