package com.example.m16_architecture.di

import com.example.m16_architecture.data.UsefulActivitiesRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideUsefulActivitiesRepository() : UsefulActivitiesRepository {
        return UsefulActivitiesRepository()
    }


}