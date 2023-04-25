package com.example.m17_recyclerview.ui.main

import android.content.Context
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov"

class Repository(applicationContext: Context?) {
    private val moshi =
        Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val getMarsPhotos: MarsPhotosApi = retrofit.create(MarsPhotosApi::class.java)
}

interface MarsPhotosApi {
    @GET("/mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getPhotos(
        @Query("sol") sol: Int,
        @Query("api_key") api_key: String
    ): Response<Results>?
}