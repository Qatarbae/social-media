package com.eltex.androidschool.model

sealed interface PostStatus {
    data class Idle(val finished: Boolean = false) : PostStatus
    data object Refreshing : PostStatus
    data object EmptyLoading : PostStatus
    data object NextPageLoading : PostStatus
    data object PrevPageLoading : PostStatus
    data class EmptyError(val reason: Throwable) : PostStatus
    data class NextPageError(val reason: Throwable) : PostStatus
    data class PrevPageError(val reason: Throwable) : PostStatus
}
