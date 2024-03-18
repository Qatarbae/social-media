package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.model.UserUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UserViewModel.Factory::class)
class UserViewModel @AssistedInject constructor(
    private val store: UserStore,
    @Assisted filter: List<Long>,
) : ViewModel() {

    val uiState: StateFlow<UserUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }

        store.accept(UserMessage.ApplyFilters(filter))
    }

    fun accept(message: UserMessage) = store.accept(message)

    @AssistedFactory
    interface Factory {
        fun create(filter: List<Long>): UserViewModel
    }
}
