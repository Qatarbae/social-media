package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.UserEffect
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.model.UserUiState
import com.eltex.androidschool.mvi.Store

typealias UserStore = Store<UserUiState, UserMessage, UserEffect>
