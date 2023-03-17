package com.example.m2_layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m2_layout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myCustomRelativeView.setText(
            "Верхняя строчка, настроенная из кода",
            "нижняя строчка, настроенная из кода"
        )
    }
}