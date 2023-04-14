package com.example.m16_architecture.data

import retrofit2.Response
import retrofit2.http.GET

interface GetUsefulActivity {
    @GET("activity/")
    suspend fun getUsefulActivity(): Response<UsefulActivityDto>
}