package com.example.m3_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.m3_components.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var countNumber = 10
    private var job: Job? = null
    var isRunning = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sliderBar.addOnChangeListener { _, _, _ -> updateUI(TimerState.Idle()) }

        binding.actionButton.setOnClickListener {
            actionTimer(countNumber)
        }
    }

    private fun updateUI(state: TimerState, timerValue: Int? = null) {
        when (state) {
            is TimerState.Idle -> {
                countNumber = binding.sliderBar.value.toInt()
                binding.timerNumber.text = binding.sliderBar.value.toInt().toString()
                binding.progressBar.max = binding.sliderBar.value.toInt()
                binding.progressBar.progress = binding.sliderBar.value.toInt()
                isRunning = false
            }
            is TimerState.Run -> {
                isRunning = true
                binding.actionButton.setText(R.string.stop_timer)
                binding.sliderBar.isEnabled = false
            }
            is TimerState.Stop -> {
                isRunning = false
                binding.timerNumber.text = binding.sliderBar.value.toInt().toString()
                binding.progressBar.progress = countNumber
                binding.actionButton.setText(R.string.start_timer)
                binding.sliderBar.isEnabled = true
                Toast.makeText(this, "Таймер остановлен", Toast.LENGTH_SHORT).show()

            }
            is TimerState.Finish -> {
                isRunning = false
                binding.timerNumber.text = binding.sliderBar.value.toInt().toString()
                binding.progressBar.progress = countNumber
                binding.actionButton.setText(R.string.start_timer)
                binding.sliderBar.isEnabled = true
                Toast.makeText(this, "Таймер завершен", Toast.LENGTH_SHORT).show()
            }
            is TimerState.UpdateProgress -> {
                binding.timerNumber.text = timerValue.toString()
                binding.progressBar.progress = timerValue.toString().toInt()
            }
        }
    }


    private fun actionTimer(countNumber: Int) {
        if (!isRunning) {
            updateUI(TimerState.Run())
            job = CoroutineScope(Dispatchers.Main).launch {
                for (timerValue in countNumber downTo 1) {
                    updateUI(TimerState.UpdateProgress(), timerValue)
                    delay(1000)
                }
                updateUI(TimerState.Finish())
                cancel()
            }
        } else {
            job?.cancel()
            updateUI(TimerState.Stop())
        }
    }


    sealed class TimerState {
        class Stop : TimerState()
        class Run : TimerState()
        class Finish : TimerState()
        class Idle : TimerState()
        class UpdateProgress : TimerState()
    }
}


