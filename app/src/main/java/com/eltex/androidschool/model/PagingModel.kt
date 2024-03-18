package com.eltex.androidschool.model

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data class Loading(val value: Int = 0) : PagingModel<Nothing>
    data class Error(val reason: Throwable) : PagingModel<Nothing>
}
