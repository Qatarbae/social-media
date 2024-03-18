package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.UserEffect
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.model.UserStatus
import com.eltex.androidschool.model.UserUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.util.Either
import javax.inject.Inject

class UserReducer @Inject constructor() : Reducer<UserUiState, UserEffect, UserMessage> {
    private companion object {
        const val INITIAL_SIZE = 15
    }

    override fun reduce(
        old: UserUiState,
        message: UserMessage,
    ): ReducerResult<UserUiState, UserEffect> = when (message) {
        UserMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.users.isEmpty()) {
                    UserStatus.EmptyLoading
                } else {
                    UserStatus.Refreshing
                }
            ),
            UserEffect.LoadInitialPage,
        )

        UserMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is UserMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    if (old.users.isEmpty()) {
                        old.copy(status = UserStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    users = if (old.filters.isEmpty()) {
                        result.value
                    } else {
                        result.value.filter { it.id in old.filters }
                    },
                    status = UserStatus.Idle(result.value.size < INITIAL_SIZE),
                )
            }
        )

        is UserMessage.Check -> ReducerResult(old.copy(users = old.users.map {
            if (it.id == message.userId) {
                it.copy(checked = !it.checked)
            } else {
                it
            }
        }))

        is UserMessage.ApplyFilters -> ReducerResult(old.copy(filters = message.filters))
    }
}
