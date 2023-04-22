package com.example.m17_recyclerview.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

private const val API_KEY = "4Ui40o66miAEyQkrVkHFQ2mAaZlPLIiHmVQlzE7l"
private const val SOL = 1000

class MainViewModel(val repository: Repository) : ViewModel() {

    private var _photosStateFlow = MutableStateFlow<Response<Results>?>(null)
    val photosStateFlow = _photosStateFlow.asStateFlow()
    private var _errorMessageFlow = MutableStateFlow<String?>(null)
    val errorMessageFlow = _errorMessageFlow.asStateFlow()

    suspend fun getPhotosList() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getMarsPhotos.getPhotos(SOL, API_KEY)
            }.fold(
                onSuccess = { _photosStateFlow.value = it },
                onFailure = { _errorMessageFlow.value = it.message }
            )
        }
    }

}