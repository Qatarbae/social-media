package com.eltex.androidschool.model

data class UserUiModel(
    val id: Long = 0L,
    val login: String = "",
    val name: String = "",
    val avatar: String? = null,
    val checked: Boolean = false,
)