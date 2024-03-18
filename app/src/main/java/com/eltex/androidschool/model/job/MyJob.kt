package com.eltex.androidschool.model.job

import android.os.Parcelable
import com.eltex.androidschool.util.InstantSerializer
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
@Parcelize
data class MyJob(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("name")
    val name: String = "",
    @SerialName("position")
    val position: String = "",
    @SerialName("start")
    @Serializable(InstantSerializer::class)
    val start: Instant = Instant.now(),
    @SerialName("finish")
    @Serializable(InstantSerializer::class)
    val finish: Instant? = null,
    @SerialName("link")
    val link: String? = null,
) : Parcelable
