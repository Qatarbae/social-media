package com.eltex.androidschool.model

data class EventUiState(
    val events: List<EventUiModel> = emptyList(),
    val skeletons: Int = 0,
    val status: EventStatus = EventStatus.Idle(),
    val singleError: Throwable? = null,
)