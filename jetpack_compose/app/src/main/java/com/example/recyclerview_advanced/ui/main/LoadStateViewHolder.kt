package com.example.recyclerview_advanced.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview_advanced.R
import com.example.recyclerview_advanced.databinding.ErrorViewHolderBinding

class LoadStateViewHolder(
    private val binding: ErrorViewHolderBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessage.text = loadState.error.localizedMessage
        }
        binding.errorProgressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.error_view_holder, parent, false)
            val binding = ErrorViewHolderBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}