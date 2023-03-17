package com.example.m2_layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.m2_layout.databinding.MyCustomRelativeViewBinding

class MyCustomRelativeViewBinding
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding = MyCustomRelativeViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setText (topString: String, bottomString: String) {
        binding.topString.text = topString
        binding.bottomString.text = bottomString
    }
}