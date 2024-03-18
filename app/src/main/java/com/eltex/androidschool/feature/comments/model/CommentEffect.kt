package com.eltex.androidschool.feature.comments.model

sealed interface CommentEffect {
    data class LoadInitialPage(val postId: Long) : CommentEffect
    data class Like(val postId: Long, val comment: CommentUiModel) : CommentEffect
    data class Delete(val postId: Long, val comment: CommentUiModel) : CommentEffect
    data class Create(val postId: Long, val comment: CommentUiModel) : CommentEffect
}
