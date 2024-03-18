package com.eltex.androidschool.model

sealed interface EventStatus {
    data class Idle(val finished: Boolean = false) : EventStatus
    data object NextPageLoading : EventStatus
    data object PrevPageLoading : EventStatus
    data object InitialLoading : EventStatus
    data object Refreshing : EventStatus
    data class EmptyError(val reason: Throwable) : EventStatus
    data class NextPageError(val reason: Throwable) : EventStatus
    data class PrevPageError(val reason: Throwable) : EventStatus
}