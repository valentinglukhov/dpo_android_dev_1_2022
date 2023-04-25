package com.example.m18_permissions.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.m18_permissions.R
import com.example.m18_permissions.data.Repository
import com.example.m18_permissions.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(
                        requireContext(),
                        Repository(context?.applicationContext)
                    ) as T
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

        val adapter = PhotoAdapter(requireContext())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )

        viewModel.repository.getPhotoList().asLiveData().observe(viewLifecycleOwner) { listOfSights ->
            adapter.setData(listOfSights)
        }

        binding.takePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_photoFragment)
        }

        return binding.root
    }

}