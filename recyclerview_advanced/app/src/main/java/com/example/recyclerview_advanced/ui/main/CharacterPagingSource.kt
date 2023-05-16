package com.example.recyclerview_advanced.ui.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException

class CharacterPagingSource : PagingSource<Int, Character>() {

    private val repository: Repository = Repository()

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize
        val response = repository.getCharacterListFun(pageNumber)

        return try {
            if (response.isSuccessful) {
                val characters = response.body()?.results
                val nextPageNumber = if (characters?.isEmpty()!!) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                LoadResult.Page(characters, prevPageNumber, nextPageNumber)
            } else {
                LoadResult.Error(Exception(response.message().toString()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}