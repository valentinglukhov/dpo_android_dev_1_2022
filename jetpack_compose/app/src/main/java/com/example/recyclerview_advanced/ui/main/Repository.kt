package com.example.recyclerview_advanced.ui.main

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://rickandmortyapi.com"

class Repository() {

    suspend fun getCharacterListFun(page: Int): Response<Results> {
        delay(2000)
        return getCharacterList.getCharacterList(page)
    }

    fun characterPagingSource() = CharacterPagingSource()

    private val moshi =
        Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val getCharacterList: RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)
}

interface RickAndMortyApi {
    @GET("/api/character/")
    suspend fun getCharacterList(@Query("page") page: Int): Response<Results>

    companion object {
        const val pageSize = 20
    }
}

@JsonClass(generateAdapter = true)
class Results(
    @Json(name = "results") val results: List<Character>
)

class Character(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "location") val location: Location,
    @Json(name = "image") val image: String,
    )

class Location(
    @Json(name = "name") val name: String,
)

