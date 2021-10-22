package de.embrandt.aostracker.ui.pregame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.embrandt.aostracker.R

class PreGameFragment : Fragment() {

    companion object {
        fun newInstance() = PreGameFragment()
    }

    private lateinit var viewModel: PreGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pre_game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PreGameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}