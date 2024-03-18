package com.eltex.androidschool.viewmodel

import android.net.Uri
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.model.UserToken

data class RegistrationUiState(
    val result: UserToken? = null,
    val status: Status = Status.Loading,
    val file: Uri? = null,
    val valid: Boolean = false,
)
