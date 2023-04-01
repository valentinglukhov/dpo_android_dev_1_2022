package com.example.m11_timer_data_storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m11_timer_data_storage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = Repository(this)
        val editText = binding.editText
        binding.textView.text = repository.getText()

        binding.saveButton.setOnClickListener {
            repository.saveText(editText.text.toString())
            binding.textView.text = repository.getText()
        }
        binding.resetButton.setOnClickListener {
            repository.clearText()
            binding.textView.text = repository.getText()
        }
    }
}