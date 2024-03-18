package com.eltex.androidschool.model

data class PostUiModel(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val coords: Coordinates? = null,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val mentioned: Int = 0,
    val attachment: Attachment? = null,
    val authorAvatar: String? = null,
    val playedAudio: Boolean = false
)
