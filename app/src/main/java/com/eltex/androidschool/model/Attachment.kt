package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Attachment(
    @SerialName("url")
    val url: String,
    @SerialName("type")
    val attachmentType: AttachmentType,
) : Parcelable