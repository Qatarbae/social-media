package com.eltex.androidschool.model

import com.eltex.androidschool.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Post(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("authorAvatar")
    val authorAvatar: String? = null,
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("coords")
    val coords: Coordinates? = null,
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("mentionIds")
    val mentionIds: List<Long> = emptyList(),
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("attachment")
    val attachment: Attachment? = null,
)
