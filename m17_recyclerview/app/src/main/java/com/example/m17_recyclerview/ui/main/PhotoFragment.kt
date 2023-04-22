package com.example.m17_recyclerview.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.m17_recyclerview.R
import com.example.m17_recyclerview.databinding.FragmentPhotoBinding

private const val PHOTO = "photo"

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private var _photo: Photo? = null
    private var photo = _photo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        arguments?.let {
            photo = it.getParcelable(PHOTO)!!
        }
        if (photo != null) {
            Glide
                .with(this@PhotoFragment)
                .load(photo!!.img_src)
                .into(binding.photo)
            binding.camera.text = requireContext().getString(R.string.camera, photo!!.camera.name)
            binding.earthDate.text =
                requireContext().getString(R.string.earth_date, photo!!.earth_date)
            binding.sol.text = requireContext().getString(R.string.sol, photo!!.sol)
            binding.rover.text = requireContext().getString(R.string.rover, photo!!.rover.name)
        }
        return binding.root
    }
}