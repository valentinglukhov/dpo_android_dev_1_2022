package com.example.recyclerview_advanced.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.recyclerview_advanced.R
import com.example.recyclerview_advanced.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(Repository()) as T
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CharacterAdapter(requireContext())
        val footer = CharacterLoadStateAdapter { adapter.retry() }
        val header = CharacterLoadStateAdapter { adapter.retry() }
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(header, footer)

        val characterData = viewModel.characters

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterData.collectLatest { characters ->
                    adapter.submitData(characters)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { state ->
                    binding.recyclerView.isVisible = state.source.refresh !is LoadState.Loading
                    binding.progressBar.isVisible = state.source.refresh is LoadState.Loading
                }
            }
        }
    }
}