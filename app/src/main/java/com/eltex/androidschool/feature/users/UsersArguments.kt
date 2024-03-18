package com.eltex.androidschool.feature.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersArguments(
    val filterIds: List<Long> = emptyList(),
    val chooser: Boolean = false,
) : Parcelable
