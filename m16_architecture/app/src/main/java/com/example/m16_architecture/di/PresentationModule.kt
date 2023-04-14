package com.example.m16_architecture.di

import com.example.m16_architecture.domain.GetUsefulActivityUseCase
import com.example.m16_architecture.presentation.MainViewModel
import com.example.m16_architecture.presentation.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideMainViewModel(getUsefulActivityUseCase: GetUsefulActivityUseCase): MainViewModel {
        return MainViewModel(getUsefulActivityUseCase)
    }

    @Provides
    fun provideMainViewModelFactory(mainViewModel: MainViewModel): MainViewModelFactory {
        return MainViewModelFactory(mainViewModel)
    }


}