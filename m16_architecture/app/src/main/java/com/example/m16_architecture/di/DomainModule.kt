package com.example.m16_architecture.di

import com.example.m16_architecture.data.UsefulActivitiesRepository
import com.example.m16_architecture.domain.GetUsefulActivityUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetUsefulActivityUseCase(usefulActivitiesRepository: UsefulActivitiesRepository)
    : GetUsefulActivityUseCase {
        return GetUsefulActivityUseCase(usefulActivitiesRepository)
    }

}