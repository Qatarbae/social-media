package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.UserStatus
import com.eltex.androidschool.model.UserUiModel
import com.eltex.androidschool.model.UserUiState

class UserPagingModelMapper {
    fun map(state: UserUiState): List<PagingModel<UserUiModel>> {
        val users = state.users.map {
            PagingModel.Data(it)
        }

        return when (val status = state.status) {
            is UserStatus.EmptyError -> users + PagingModel.Error(status.reason)
            else -> users
        }
    }
}
