package com.eltex.androidschool.model

import com.eltex.androidschool.util.InstantSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,

    @SerialName("authorId")
    val authorId: Long = 0L,

    @SerialName("author")
    val author: String = "",

    @SerialName("authorJob")
    val authorJob: String? = null,

    @SerialName("authorAvatar")
    val authorAvatar: String? = null,

    @SerialName("content")
    val content: String = "",

    @SerialName("datetime")
    @EncodeDefault
    @Serializable(InstantSerializer::class)
    val datetime: Instant = Instant.now(),

    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),

    @SerialName("coords")
    val coords: Coordinates? = null,

    @SerialName("type")
    val type: String = "",

    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),

    @SerialName("likedByMe")
    val likedByMe: Boolean = false,

    @SerialName("speakerIds")
    val speakerIds: Set<Long> = emptySet(),

    @SerialName("participantsIds")
    val participantsIds: Set<Long> = emptySet(),

    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,

    @SerialName("attachment")
    val attachment: Attachment? = null,

    @SerialName("link")
    val link: String? = null,

    @SerialName("users")
    val users: Map<Long, UserPreview> = emptyMap()
)
