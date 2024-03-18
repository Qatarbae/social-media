package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AvatarModel(
    val userId: Long = 0L,
    val name: String = "",
    val avatar: String? = null,
) : Parcelable