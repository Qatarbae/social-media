package com.eltex.androidschool.model

data class PostWithError(
    val post: PostUiModel,
    val throwable: Throwable,
)
