package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserPreview(
    @SerialName("name")
    val name: String = "",

    @SerialName("avatar")
    val avatar: String? = null,
) : Parcelable
