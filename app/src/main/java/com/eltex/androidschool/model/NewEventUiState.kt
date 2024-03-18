package com.eltex.androidschool.model

data class NewEventUiState(
    val status: Status = Status.Idle,
    val result: Event? = null,
    val file: FileModel? = null,
    val coords: Coordinates? = null,
)