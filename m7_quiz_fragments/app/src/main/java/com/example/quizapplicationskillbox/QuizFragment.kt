package com.example.quizapplicationskillbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizapplicationskillbox.databinding.FragmentQuizBinding
import com.example.quizapplicationskillbox.quiz.QuizStorage

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var results = mutableListOf<Int>()
    private var radioGroupList: MutableList<RadioGroup> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater)
        val bundle = Bundle()
        radioGroupList = inflateRadioGroup()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_mainFragment)
        }

        binding.sendButton.setOnClickListener {
            results = getResultFromRadioGroup(radioGroupList)
            if (results.size < 3) {
                Toast.makeText(
                    this.context,
                    "Пожалуйста ответьте на все вопросы =)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val quizAnswers =
                QuizStorage.answer(QuizStorage.getQuiz(QuizStorage.Locale.Ru), results)
            bundle.putString("quizAnswers", quizAnswers)
            findNavController().navigate(
                R.id.action_quizFragment_to_resultsFragment, args = bundle
            )
        }
        return binding.root
    }

    fun getResultFromRadioGroup(radioGroupList: MutableList<RadioGroup>): MutableList<Int> {
        val resultList: MutableList<Int> = mutableListOf()
        radioGroupList.forEachIndexed { _, radioGroup ->
            if (radioGroup.checkedRadioButtonId >= 0) resultList.add(radioGroup.checkedRadioButtonId)
        }
        return resultList
    }


    fun inflateRadioGroup(): MutableList<RadioGroup> {
        radioGroupList.clear()
        val paddingDp = 16
        val paddingPx = (resources.displayMetrics.density * paddingDp + 0.5F).toInt()
        var radioGroup: RadioGroup
        val linearLayout = binding.linearLayout
        val questions = QuizStorage.getQuiz(QuizStorage.Locale.Ru).questions
        for (question in questions.indices) {
            var answerId = 0
            radioGroup = RadioGroup(this.context)
            radioGroup.orientation = RadioGroup.VERTICAL
            val questionText = TextView(this.context)
            questionText.text = questions[question].question
            questionText.id = View.generateViewId()
            questionText.setPadding(paddingPx)
            radioGroup.addView(questionText)
            for (answer in questions[question].answers.indices) {
                val radioButton = RadioButton(this.context)
                radioButton.text = questions[question].answers[answer]
                radioButton.id = answerId
                radioGroup.addView(radioButton)
                answerId++
            }
            linearLayout.addView(radioGroup)
            radioGroupList.add(radioGroup)
        }
        return radioGroupList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        results.clear()
        radioGroupList.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}