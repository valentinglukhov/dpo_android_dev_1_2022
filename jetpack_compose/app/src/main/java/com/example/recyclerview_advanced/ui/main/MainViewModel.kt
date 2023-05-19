package com.example.recyclerview_advanced.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class MainViewModel(val repository: Repository) : ViewModel() {

    val pagerConfig = PagingConfig(pageSize = RickAndMortyApi.pageSize, enablePlaceholders = false)

    val characters: Flow<PagingData<Character>> =
        Pager(config = pagerConfig, pagingSourceFactory = { repository.characterPagingSource() })
            .flow
            .cachedIn(viewModelScope)

}