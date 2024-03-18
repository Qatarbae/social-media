package com.eltex.androidschool.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("token")
    val token: String,
)
