package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.NewEventUiState
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NewEventViewModelFactory::class)
class NewEventViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    @Assisted private val id: Long,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()
    fun save(content: String) {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val result =
                    repository.saveEvent(id, content, _state.value.file, _state.value.coords)
                _state.update {
                    it.copy(
                        status = Status.Idle,
                        result = result
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
    }

    fun saveFile(fileModel: FileModel?) = _state.update {
        it.copy(file = fileModel)
    }

    fun saveCoords(coords: Coordinates) = _state.update {
        it.copy(coords = coords)
    }

    fun handleError() {
        _state.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }
}