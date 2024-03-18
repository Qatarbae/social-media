package com.eltex.androidschool.feature.comments.model

data class CommentUiModel(
    val id: Long = 0,
    val postId: Long = 0,
    val author: String = "",
    val authorAvatar: String? = null,
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)