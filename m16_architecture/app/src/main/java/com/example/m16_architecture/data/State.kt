package com.example.m16_architecture.data

sealed class State (open val value: String) {
    class Success(override val value: String) : State(value)
    class Failure(override val value: String) : State(value)
}
