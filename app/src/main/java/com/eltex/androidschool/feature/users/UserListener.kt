package com.eltex.androidschool.feature.users

import com.eltex.androidschool.model.UserUiModel

interface UserListener {
    fun onRefresh()
    fun checkedListener(user: UserUiModel)
}
