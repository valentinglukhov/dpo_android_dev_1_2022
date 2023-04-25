package com.example.m17_recyclerview.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m17_recyclerview.R
import com.example.m17_recyclerview.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

private const val PHOTO = "photo"

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        val photoAdapter = PhotoAdapter(requireContext(), {photo -> onClick(photo!!)})

        binding.photoRV.adapter = photoAdapter
        binding.photoRV.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPhotosList()
            viewModel.photosStateFlow.collect { results ->
                Log.d("Fragment", (results == null).toString())
                if (results != null) photoAdapter.setData(results)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessageFlow.collect {error ->
                if (error != null) Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun onClick(photo: Photo) {
        val bundle = Bundle().apply {
            putParcelable(PHOTO, photo)
        }
        findNavController().navigate(resId = R.id.action_mainFragment_to_photoFragment, args = bundle)
    }
}