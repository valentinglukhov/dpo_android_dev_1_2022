package com.example.m17_recyclerview.ui.main

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "photos") val photos: List<Photo>
) : Parcelable

@Parcelize
data class Photo(
    @Json(name = "id") val id: Int,
    @Json(name = "sol") val sol: Int,
    @Json(name = "camera") val camera: Camera,
    @Json(name = "img_src") val img_src: String,
    @Json(name = "earth_date") val earth_date: String,
    @Json(name = "rover") val rover: Rover
) : Parcelable

@Parcelize
data class Camera(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "rover_id") val rover_id: Int,
    @Json(name = "full_name") val full_name: String
) : Parcelable

@Parcelize
data class Rover(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "landing_date") val landing_date: String,
    @Json(name = "launch_date") val launch_date: String,
    @Json(name = "status") val status: String
) : Parcelable
