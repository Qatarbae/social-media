package com.eltex.androidschool.feature.comments.model

import com.eltex.androidschool.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Comment(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("postId")
    val postId: Long = 0,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorAvatar")
    val authorAvatar: String? = null,
    @SerialName("content")
    val content: String = "",
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
)
