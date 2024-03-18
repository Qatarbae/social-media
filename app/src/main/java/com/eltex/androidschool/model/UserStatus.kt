package com.eltex.androidschool.model

sealed interface UserStatus {
    data class Idle(val finished: Boolean = false) : UserStatus
    data object Refreshing : UserStatus
    data object EmptyLoading : UserStatus
    data class EmptyError(val reason: Throwable) : UserStatus
}
