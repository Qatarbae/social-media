package com.eltex.androidschool.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AttachmentType {
    @SerialName("IMAGE")
    IMAGE,

    @SerialName("VIDEO")
    VIDEO,

    @SerialName("AUDIO")
    AUDIO,
    ;
}