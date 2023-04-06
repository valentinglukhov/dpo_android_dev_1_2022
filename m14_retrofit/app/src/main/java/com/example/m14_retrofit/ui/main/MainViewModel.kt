package com.example.m14_retrofit.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    suspend fun getJson(): UserModel {
        val result = repository.searchRandomUser.getRandomUser()
        return UserModel(
            result.body()?.results?.first()?.name?.first,
            result.body()?.results?.first()?.name?.last,
            result.body()?.results?.first()?.picture?.large
        )
    }
}