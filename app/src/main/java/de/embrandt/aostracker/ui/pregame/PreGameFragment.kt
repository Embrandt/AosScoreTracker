package de.embrandt.aostracker.ui.pregame

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import de.embrandt.aostracker.databinding.FragmentPregameBinding
import java.time.LocalDate
import java.util.*

class PreGameFragment : Fragment() {

    private val viewModel: PreGameViewModel by activityViewModels()
    private var _binding: FragmentPregameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("PregameFragment", "onCreateView")
        _binding = FragmentPregameBinding.inflate(inflater, container, false)

        binding.pregameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.battleDateLayout.setEndIconOnClickListener { selectDate() }
//        viewModel.battleDateText.observe(viewLifecycleOwner, {
//            binding.battleDate.setText(it)
//            Log.i("PregameFragment", "change observed")
//        })
        return binding.root
    }

    private fun selectDate() {
        Log.i("PregameFragment", "open dialog here")
        val newFragment = DatePickerFragment()
        newFragment.show(parentFragmentManager, "datepicker")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: PreGameViewModel by activityViewModels()
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.setBattleDate(year, month + 1, dayOfMonth)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)

    }

}