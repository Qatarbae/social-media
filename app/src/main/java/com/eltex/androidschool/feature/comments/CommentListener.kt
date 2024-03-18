package com.eltex.androidschool.feature.comments

import com.eltex.androidschool.feature.comments.model.CommentUiModel

interface CommentListener {
    fun onLikeClicked(comment: CommentUiModel)
    fun onDeleteClicked(comment: CommentUiModel)
    fun onRefresh()
    fun retryClicked()
}