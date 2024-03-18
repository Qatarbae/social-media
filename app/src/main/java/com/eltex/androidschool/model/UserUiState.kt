package com.eltex.androidschool.model

data class UserUiState(
    val users: List<UserUiModel> = emptyList(),
    val status: UserStatus = UserStatus.Idle(),
    val filters: List<Long> = emptyList(),
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean = status == UserStatus.Refreshing

    val emptyError: Throwable? = (status as? UserStatus.EmptyError)?.reason

    val isEmptyLoading: Boolean = status == UserStatus.EmptyLoading
}
