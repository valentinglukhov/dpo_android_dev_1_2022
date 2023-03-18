package com.example.m3_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.m3_components.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var countNumber = 10
    private var job: Job? = null
    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sliderBar.addOnChangeListener { _, value, _ ->
            countNumber = value.toInt()
            binding.timerNumber.text = value.toInt().toString()
            binding.progressBar.max = value.toInt()
            binding.progressBar.progress = value.toInt()
            isRunning = false
        }

        binding.actionButton.setOnClickListener {
            getTimerNumber()
            actionTimer(countNumber)
        }
    }

    private fun getTimerNumber() = findViewById<TextView>(R.id.timerNumber)


    private fun actionTimer(countNumber: Int) {
        val timerNumber = findViewById<TextView>(R.id.timerNumber)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val sliderBar = findViewById<Slider>(R.id.sliderBar)
        val actionButton = findViewById<Button>(R.id.actionButton)
        if (!isRunning) {
            isRunning = true
            actionButton.setText(R.string.stop_timer)
            sliderBar.isEnabled = false
            job = CoroutineScope(Dispatchers.Main).launch {
                for (i in countNumber downTo 0) {
                    timerNumber.text = i.toString()
                    progressBar.progress = i
                    if (i > 0) delay(1000)
                }
                isRunning = false
                timerNumber.text = sliderBar.value.toInt().toString()
                progressBar.progress = countNumber
                actionButton.setText(R.string.start_timer)
                sliderBar.isEnabled = true
                Toast.makeText(this@MainActivity, "Таймер завершен", Toast.LENGTH_SHORT).show()
                cancel()
            }
        } else {
            job?.cancel()
            isRunning = false
            timerNumber.text = sliderBar.value.toInt().toString()
            progressBar.progress = countNumber
            actionButton.setText(R.string.start_timer)
            sliderBar.isEnabled = true
            Toast.makeText(this@MainActivity, "Таймер остановлен", Toast.LENGTH_SHORT).show()
        }
    }


    sealed class TimerState {
        class Stopped : TimerState()
        class Running : TimerState()
        class Finished : TimerState()

        fun action (action: TimerState) {
            when(action) {
                is Stopped -> {

                }

                else -> {}
            }
        }
    }
}


