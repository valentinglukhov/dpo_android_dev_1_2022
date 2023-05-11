package com.example.views_and_animations.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.views_and_animations.R
import com.example.views_and_animations.databinding.FragmentMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var listeners = mutableSetOf<(TimeState) -> Unit>()
    private var currentTimeState: TimeState? = null
    private var isRunning: Boolean = false

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel() as T
                } else {
                    throw IllegalArgumentException("")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        addListener(binding.clock::invalidateClock)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRunning.collect {
                isRunning = it
            }
        }

        if (!isRunning && viewModel.timerTimeStamp == null) viewModel.startClock()
        if (isRunning) binding.startStop.text = getString(R.string.stop)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentTimeState.collect { timeState ->
                currentTimeState = timeState
                listeners.forEach { clock ->
                    if (timeState != null) {
                        clock(timeState)
                    }
                }
                binding.clockValue.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(timeState?.timeStamp ?: 0)
            }
        }

        binding.startStop.setOnClickListener {
            when (isRunning) {
                true -> {
                    viewModel.stopTimer()
                    binding.startStop.text = getString(R.string.start)
                }
                false -> {
                    viewModel.startTimer()
                    binding.startStop.text = getString(R.string.stop)
                }
            }
        }

        binding.reset.setOnClickListener {
            viewModel.resetTimer()
            binding.startStop.text = getString(R.string.start)
        }
        return binding.root
    }

    fun addListener(listener: (TimeState) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (TimeState) -> Unit) {
        listeners.remove(listener)
    }

}