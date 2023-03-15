package com.example.passengercounter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.passengercounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.minusButton.setOnClickListener {
            counter(0, binding)
        }
        binding.plusButton.setOnClickListener {
            counter(1, binding)
        }

        binding.resetButton.setOnClickListener {
            counter = 0
            binding.passengerCounter.text = counter.toString()
            binding.minusButton.isEnabled = false
            binding.minusButton.alpha = 0.5F
            binding.resetButton.visibility = View.INVISIBLE
            binding.stateInfo.text = getText(R.string.free)
            binding.stateInfo.setTextColor(Color.parseColor("#03BA22"))
        }



    }

    fun counter(action: Int, binding: ActivityMainBinding) {
        when (action) {
            1 -> {
                if (counter < 50) counter++
                binding.passengerCounter.text = counter.toString()
            }
            0 -> {
                if (counter > 0) counter--
                binding.passengerCounter.text = counter.toString()
            }
        }
        when (counter) {
            0 -> {
                binding.stateInfo.text = getText(R.string.free)
                binding.stateInfo.setTextColor(Color.parseColor("#03BA22"))
                binding.minusButton.isEnabled = false
                binding.minusButton.alpha = 0.5F
                binding.resetButton.visibility = View.INVISIBLE
            }
            in 1..49 -> {
                binding.stateInfo.text = "Осталось мест: ${50 - counter}"
                binding.stateInfo.setTextColor(Color.parseColor("#3F45F0"))
                binding.minusButton.isEnabled = true
                binding.minusButton.alpha = 1F
                binding.resetButton.visibility = View.INVISIBLE
            }
            50 -> {
                binding.stateInfo.text = getText(R.string.too_much)
                binding.stateInfo.setTextColor(Color.parseColor("#FF3737"))
                binding.minusButton.isEnabled = true
                binding.minusButton.alpha = 1F
                binding.resetButton.visibility = View.VISIBLE
            }
        }
    }
}