package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventDetailsUiState
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EventDetailsViewModelFactory::class)
class EventDetailsViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
    @Assisted private val event: EventUiModel,
) : ViewModel() {
    private val _state = MutableStateFlow(EventDetailsUiState(event))
    val state = _state.asStateFlow()

    fun like() {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val event = requireNotNull(state.value.event)
                val result = if (event.likedByMe) {
                    repository.unlikeById(event.id)
                } else {
                    repository.likeById(event.id)
                }
                _state.update {
                    it.copy(
                        status = Status.Idle,
                        event = mapper.map(result),
                        isLikePressed = true,
                        isParticipatePressed = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
    }

    fun participate() {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val event = requireNotNull(state.value.event)
                val result = if (event.participatedByMe) {
                    repository.unparticipateById(event.id)
                } else {
                    repository.participateById(event.id)
                }
                _state.update {
                    it.copy(
                        status = Status.Idle,
                        event = mapper.map(result),
                        isLikePressed = false,
                        isParticipatePressed = true
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
        }
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