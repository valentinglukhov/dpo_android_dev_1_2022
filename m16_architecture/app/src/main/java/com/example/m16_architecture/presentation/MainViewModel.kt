package com.example.m16_architecture.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.m16_architecture.data.UsefulActivitiesRepository
import com.example.m16_architecture.data.UsefulActivityDto
import com.example.m16_architecture.domain.GetUsefulActivityUseCase
import com.example.m16_architecture.entity.UsefulActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUsefulActivityUseCase: GetUsefulActivityUseCase
) : ViewModel() {

    private var _activityStateFlow: MutableStateFlow<UsefulActivity>? = null
    val activityStateFlow = _activityStateFlow?.asStateFlow()


    suspend fun reloadUsefulActivity() {
        val usefulActivity = getUsefulActivityUseCase.execute()
        _activityStateFlow?.value = usefulActivity
        Log.d("ViewModel:", usefulActivity.toString())

    }
}
