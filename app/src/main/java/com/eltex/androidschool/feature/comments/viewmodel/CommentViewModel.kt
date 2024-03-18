package com.eltex.androidschool.feature.comments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.store.CommentStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val store: CommentStore,
) : ViewModel() {

    val uiState: StateFlow<CommentUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: CommentMessage) = store.accept(message)
}
