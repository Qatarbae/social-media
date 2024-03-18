package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.NewPostUiState
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.postrepository.PostRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NewPostViewModelFactory::class)
class NewPostViewModel @AssistedInject constructor(
    private val repository: PostRepository,
    @Assisted
    private val postId: Long = 0,
) : ViewModel() {

    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun savePost(content: String) {
        viewModelScope.launch {
            try {
                val post = repository.savePost(
                    postId,
                    content,
                    _state.value.file,
                    _state.value.coordinates,
                    _state.value.mentionIds
                )
                _state.update { it.copy(result = post, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
    }

    fun setFile(fileModel: FileModel?) = _state.update {
        it.copy(file = fileModel)
    }

    fun setCoordinates(coordinates: Coordinates?) = _state.update {
        it.copy(coordinates = coordinates)
    }

    fun setMentionIds(mentionIds: List<Long>) = _state.update {
        it.copy(mentionIds = mentionIds)
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}
