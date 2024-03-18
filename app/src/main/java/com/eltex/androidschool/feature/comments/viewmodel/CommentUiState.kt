package com.eltex.androidschool.feature.comments.viewmodel

import com.eltex.androidschool.feature.comments.model.CommentStatus
import com.eltex.androidschool.feature.comments.model.CommentUiModel

data class CommentUiState(
    val comments: List<CommentUiModel> = emptyList(),
    val status: CommentStatus = CommentStatus.Idle(),
    val singleError: Throwable? = null,
)
