package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Coordinates(
    @SerialName("lat")
    val lat: Double = 0.0,

    @SerialName("long")
    val long: Double = 0.0,
) : Parcelable