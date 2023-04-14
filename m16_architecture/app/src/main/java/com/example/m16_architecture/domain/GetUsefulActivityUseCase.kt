package com.example.m16_architecture.domain

import com.example.m16_architecture.data.UsefulActivitiesRepository
import com.example.m16_architecture.entity.UsefulActivity
import kotlinx.coroutines.flow.Flow

class GetUsefulActivityUseCase(
    private val usefulActivitiesRepository: UsefulActivitiesRepository
) {
    suspend fun execute(): UsefulActivity {
        return usefulActivitiesRepository.getUsefulActivity()
    }
}