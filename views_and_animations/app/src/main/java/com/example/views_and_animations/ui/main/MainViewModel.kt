package com.example.views_and_animations.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private var coroutineScopes = mutableListOf<Job>()
    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()
    var timerTimeStamp: Long? = null

    private var _currentTimeState = MutableStateFlow<TimeState?>(null)
    val currentTimeState = _currentTimeState.asStateFlow()

    private fun cancelJobs() {
        coroutineScopes.forEach { job ->
            job.cancel()
        }
    }

    fun startClock() {
        cancelJobs()
        _isRunning.value = false
        val clockJob = viewModelScope.launch {
            isRunning.collect {
                while (!it) {
                    delay(500)
                    _currentTimeState.value = TimeState(System.currentTimeMillis(), it)
                }
            }
        }
        coroutineScopes.add(clockJob)
    }

    fun resetTimer() {
        cancelJobs()
        timerTimeStamp = null
        startClock()
    }

    fun stopTimer() {
        cancelJobs()
        _isRunning.value = false
    }

    fun startTimer() {
        val offset: Int = TimeZone.getDefault().rawOffset + TimeZone.getDefault().dstSavings
        cancelJobs()
        val startTimeInMillis = if (timerTimeStamp == null) {
            System.currentTimeMillis() + offset
        } else {
            System.currentTimeMillis() - timerTimeStamp!!
        }
        _isRunning.value = true
        val timerJob = viewModelScope.launch {
            isRunning.collect {
                while (it) {
                    delay(500)
                    val timeState = TimeState(System.currentTimeMillis() - startTimeInMillis, it)
                    timerTimeStamp = timeState.timeStamp
                    _currentTimeState.value = timeState
                }
            }
        }
        coroutineScopes.add(timerJob)
    }

}