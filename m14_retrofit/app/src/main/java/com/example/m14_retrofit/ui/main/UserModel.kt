package com.example.m14_retrofit.ui.main

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel(
    var firstName: String?,
    var lastName: String?,
    var photoUrl: String?
) : Parcelable

@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "results") val results: List<User>,
)

data class User(
    @Json(name = "name") val name: Name,
    @Json(name = "picture") val picture: Picture,
)

data class Name(
    @Json(name = "first") val first: String,
    @Json(name = "last") val last: String,
)

data class Picture(
    @Json(name = "large") val large: String,
)
