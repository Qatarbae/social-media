package com.eltex.androidschool.model

data class NewPostUiState(
    val result: Post? = null,
    val status: Status = Status.Idle,
    val file: FileModel? = null,
    val coordinates: Coordinates? = null,
    val mentionIds: List<Long> = emptyList()
)