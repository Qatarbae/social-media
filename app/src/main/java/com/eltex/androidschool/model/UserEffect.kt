package com.eltex.androidschool.model

sealed interface UserEffect {
    data object LoadInitialPage : UserEffect
}
