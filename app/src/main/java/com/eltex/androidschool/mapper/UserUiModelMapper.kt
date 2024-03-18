package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.User
import com.eltex.androidschool.model.UserUiModel
import javax.inject.Inject

class UserUiModelMapper @Inject constructor() {
    fun map(user: User): UserUiModel = with(user) {
        UserUiModel(
            id = id,
            login = login,
            name = name,
            avatar = avatar,
        )
    }
}