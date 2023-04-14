package com.example.m16_architecture.domain

import com.example.m16_architecture.data.UsefulActivitiesRepository
import com.example.m16_architecture.entity.UsefulActivity

class GetUsefulActivityUseCase(
    private val usefulActivitiesRepository: UsefulActivitiesRepository
) {
    suspend fun execute(): UsefulActivity {
        return usefulActivitiesRepository.getUsefulActivity()
    }
}