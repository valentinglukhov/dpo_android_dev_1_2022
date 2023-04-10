package com.example.room.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.room.R
import com.example.room.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var currentList: List<Word>
    private val regexFilter = "[a-zA-Zа-яА-Я-']+"


    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(Repository(context?.applicationContext)) as T
                } else {
                    throw IllegalArgumentException("")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel.getList().asLiveData().observe(viewLifecycleOwner) { list ->
            currentList = list
            binding.wordsList.text = "Слово (Количество повторений)\n"
            list.forEach { word ->
                val wordsList = "${word.value} - (${word.repetition})\n"
                binding.wordsList.append(wordsList)
            }
        }
        binding.addButton.setOnClickListener {
            val matchResult = Regex(regexFilter).matches(binding.editText.text)
            when (matchResult) {
                true -> {
                    val word = Word(binding.editText.text.toString(), 1)
                    binding.editText.text.clear()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.insertOrUpdate(word)
                    }
                }
                false -> Toast.makeText(
                    requireContext(),
                    getString(R.string.regex_toast_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.clearButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.clearDb()
            }
        }
        return binding.root
    }
}