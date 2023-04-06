package com.example.m14_retrofit.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.m14_retrofit.R
import com.example.m14_retrofit.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

private const val USER_MODEL = "userModel"

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private var userModel: UserModel? = null
    private var nameField: TextView? = null
    private var imageView: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        nameField = binding.nameField
        imageView = binding.imageView
        savedInstanceState?.let {
            userModel = it.getParcelable(USER_MODEL)
        }

        if (userModel == null) {
            lifecycleScope.launch {
                try {
                    userModel = viewModel.getJson()
                    if (userModel != null) {
                        updateUi(userModel!!)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            updateUi(userModel!!)
        }

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    userModel = viewModel.getJson()
                    if (userModel != null) {
                        updateUi(userModel!!)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (userModel != null) outState.putParcelable(USER_MODEL, userModel)
        super.onSaveInstanceState(outState)
    }

    fun updateUi(userModel: UserModel) {
        nameField?.text = getString(R.string.user_name, userModel.firstName, userModel.lastName)
        Glide.with(this).load(userModel.photoUrl).fitCenter().into(imageView!!)
    }
}

//        if (userModel == null) {
//            lifecycleScope.launch {
//                try {
//                    response = viewModel.getJson()
//                    if (response!!.isSuccessful) {
//                        firstName = response!!.body()?.results?.first()?.name?.first
//                        lastName = response!!.body()?.results?.first()?.name?.last
//                        url = response!!.body()?.results?.first()?.picture?.large
//                        Glide.with(this@MainFragment)
//                            .load(url)
//                            .fitCenter()
//                            .into(imageView)
//                        nameField.text = getString(R.string.user_name, firstName, lastName)
//                        userModel = UserModel(firstName, lastName, url)
//                    }
//                } catch (e: Exception) {
//                    Log.d("MainFragment", e.message.toString())
//                }
//            }
//        } else {
//            nameField.text =
//                getString(R.string.user_name, userModel!!.firstName, userModel!!.lastName)
//            Glide.with(this)
//                .load(userModel!!.photoUrl)
//                .fitCenter()
//                .into(imageView)
//        }
//