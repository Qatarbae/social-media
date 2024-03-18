package com.eltex.androidschool.model

import com.eltex.androidschool.util.Either

sealed interface UserMessage {
    data class ApplyFilters(val filters: List<Long>) : UserMessage
    data object Refresh : UserMessage
    data class Check(val userId: Long) : UserMessage
    data object HandleError : UserMessage
    data class InitialLoaded(val result: Either<Throwable, List<UserUiModel>>) : UserMessage
}
