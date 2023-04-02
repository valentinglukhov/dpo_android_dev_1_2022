package com.example.m12_mvvm

sealed class State {
    object Idle : State()
    object Loading : State()
    data class Finish(val searchText: String) : State()
}