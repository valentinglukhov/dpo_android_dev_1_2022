package com.example.m19_location.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenState(
    val latitude: Double,
    val longitude: Double,
    val zoom: Float,
    val azimuth: Float,
    val tilt: Float,
    val isDescriptionOpened: Boolean,
    val xid: String,
    val name: String
) : Parcelable
