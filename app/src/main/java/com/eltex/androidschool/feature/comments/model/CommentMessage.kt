package com.eltex.androidschool.feature.comments.model

import com.eltex.androidschool.util.Either

sealed interface CommentMessage {
    data object Retry : CommentMessage
    data class Refresh(val postId: Long) : CommentMessage
    data class Like(val postId: Long, val comment: CommentUiModel) : CommentMessage
    data class Delete(val postId: Long, val comment: CommentUiModel) : CommentMessage
    data class Create(val postId: Long, val comment: CommentUiModel) : CommentMessage
    data object HandleError : CommentMessage

    data class DeleteError(val error: CommentWithError) : CommentMessage
    data class LikeResult(val result: Either<CommentWithError, CommentUiModel>) : CommentMessage
    data class CreateResult(val result: Either<CommentWithError, CommentUiModel>) : CommentMessage
    data class InitialLoaded(val result: Either<Throwable, List<CommentUiModel>>) : CommentMessage
}