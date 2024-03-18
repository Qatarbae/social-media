package com.eltex.androidschool.feature.comments.model

data class CommentWithError(
    val comment: CommentUiModel,
    val throwable: Throwable,
)
