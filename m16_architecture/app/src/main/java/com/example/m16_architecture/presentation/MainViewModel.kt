package com.example.m16_architecture.presentation

import androidx.lifecycle.ViewModel
import com.example.m16_architecture.domain.GetUsefulActivityUseCase
import com.example.m16_architecture.entity.UsefulActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getUsefulActivityUseCase: GetUsefulActivityUseCase
) : ViewModel() {

    suspend fun reloadUsefulActivity() : StateFlow<UsefulActivity> {
        return MutableStateFlow(getUsefulActivityUseCase.execute())
    }
}
