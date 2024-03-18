package com.eltex.androidschool.feature.comments

import com.eltex.androidschool.feature.comments.model.CommentEffect
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.model.CommentStatus
import com.eltex.androidschool.feature.comments.viewmodel.CommentUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.util.Either
import javax.inject.Inject

class CommentReducer @Inject constructor() :
    Reducer<CommentUiState, CommentEffect, CommentMessage> {

    private companion object {
        const val INITIAL_SIZE = 15
    }

    override fun reduce(
        old: CommentUiState,
        message: CommentMessage,
    ): ReducerResult<CommentUiState, CommentEffect> = when (message) {
        is CommentMessage.Like -> ReducerResult(
            old.copy(
                comments = old.comments.map {
                    if (it.id == message.comment.id) {
                        it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    } else {
                        it
                    }
                }
            ),
            CommentEffect.Like(message.postId, message.comment)
        )

        CommentMessage.Retry -> {
            ReducerResult(old)
        }

        is CommentMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.comments.isEmpty()) {
                    CommentStatus.EmptyLoading
                } else {
                    CommentStatus.Refreshing
                }
            ),
            CommentEffect.LoadInitialPage(message.postId),
        )

        is CommentMessage.Delete -> ReducerResult(
            old.copy(comments = old.comments.filter { it.id != message.comment.id }),
            CommentEffect.Delete(message.postId, message.comment),
        )

        is CommentMessage.DeleteError -> with(message.error) {
            ReducerResult(
                old.copy(
                    comments = buildList(old.comments.size + 1) {
                        addAll(old.comments.takeWhile { it.id < comment.id })
                        add(comment)
                        addAll(old.comments.takeLastWhile { it.id > comment.id })
                    },
                    singleError = throwable,
                )
            )
        }

        is CommentMessage.LikeResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    val comment = value.comment
                    old.copy(
                        comments = old.comments.map {
                            if (it.id == comment.id) {
                                comment
                            } else {
                                it
                            }
                        },
                        singleError = value.throwable,
                    )
                }

                is Either.Right -> {
                    val comment = result.value
                    old.copy(
                        comments = old.comments.map {
                            if (it.id == comment.id) {
                                comment
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        )

        CommentMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is CommentMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    if (old.comments.isEmpty()) {
                        old.copy(status = CommentStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    comments = result.value,
                    status = CommentStatus.Idle(result.value.size < INITIAL_SIZE),
                )
            }
        )

        is CommentMessage.Create -> ReducerResult(
            old.copy(
                status = CommentStatus.Refreshing
            ),
            CommentEffect.Create(message.postId, message.comment)
        )

        is CommentMessage.CreateResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    old.copy(
                        singleError = value.throwable,
                        status = CommentStatus.Idle()
                    )
                }

                is Either.Right -> {
                    val comment = result.value
                    old.copy(
                        comments = old.comments + comment,
                        status = CommentStatus.Idle()
                    )
                }
            }
        )
    }
}