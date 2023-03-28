package com.example.quizapplicationskillbox

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.quizapplicationskillbox.databinding.FragmentResultsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "quizAnswers"
private const val ARG_PARAM2 = "param2"
class ResultsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var quizAnswers: String? = null
    private var param2: String? = null

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quizAnswers = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater)
        binding.againButton.setOnClickListener {
            parentFragmentManager.clearBackStack(ResultsFragment::class.java.simpleName)
            findNavController().navigate(R.id.action_resultsFragment_to_quizFragment)
        }
        val againButton = binding.againButton
        ObjectAnimator.ofFloat(againButton, View.TRANSLATION_X, -1000F, 0F)
            .apply {
                duration = 1000
                interpolator = DecelerateInterpolator()
            }.start()
        val resultsTextView = binding.resultsTextView
        ObjectAnimator.ofFloat(resultsTextView, View.TRANSLATION_X, 1000F, 0F)
            .apply {
                duration = 1000
                interpolator = DecelerateInterpolator()
            }.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resultsTextView.text = quizAnswers
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

