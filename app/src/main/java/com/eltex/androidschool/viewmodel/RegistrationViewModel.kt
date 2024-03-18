package com.eltex.androidschool.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationUiState())
    val state = _state.asStateFlow()

    fun loginAfterRegistration(login: String, pass: String, name: String, fileUri: Uri?) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val res = repository.registration(
                    login = login,
                    pass = pass,
                    name = name,
                    fileUri = fileUri,
                )
                _state.update { it.copy(result = res, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
    }

    fun saveFile(fileUri: Uri?) = _state.update {
        it.copy(file = fileUri)
    }

    fun validate(validator: Boolean) = _state.update {
        it.copy(valid = validator)
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Loading) }
    }
}