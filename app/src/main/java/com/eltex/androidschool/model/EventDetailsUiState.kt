package com.eltex.androidschool.model

data class EventDetailsUiState(
    val event: EventUiModel? = null,
    val status: Status = Status.Idle,
    val isLikePressed: Boolean = false,
    val isParticipatePressed: Boolean = false
)