package com.example.m19_location.ui.main

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.opentripmap.com/0.1/ru/places/"

class Repository() {

    private val moshi =
        Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val getSightsList: sightApi = retrofit.create(sightApi::class.java)


}

interface sightApi {
    @GET("radius")
    suspend fun getSights(
        @Query("radius") radius: Int,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("limit") limit: Int,
        @Query("apikey") apikey: String,


    ): Response<Sight>?
}
