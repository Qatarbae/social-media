package com.eltex.androidschool.feature.comments.model

sealed interface CommentStatus {
    data class Idle(val finished: Boolean = false) : CommentStatus
    data object Refreshing : CommentStatus
    data object EmptyLoading : CommentStatus
    data class EmptyError(val reason: Throwable) : CommentStatus
}
