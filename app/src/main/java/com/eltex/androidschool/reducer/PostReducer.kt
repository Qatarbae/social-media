package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.PostEffect
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostStatus
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.util.Either
import javax.inject.Inject

class PostReducer @Inject constructor() : Reducer<PostUiState, PostEffect, PostMessage> {

    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_SIZE = 15
    }

    override fun reduce(
        old: PostUiState,
        message: PostMessage,
    ): ReducerResult<PostUiState, PostEffect> = when (message) {
        is PostMessage.Like -> ReducerResult(
            old.copy(
                posts = old.posts.map {
                    if (it.id == message.post.id) {
                        it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    } else {
                        it
                    }
                }
            ),
            PostEffect.Like(message.post)
        )

        PostMessage.LoadNextPage -> {
            val nextId = old.posts.lastOrNull()?.id
            if (nextId == null || old.status !is PostStatus.Idle || old.status.finished) {
                ReducerResult(old)
            } else {
                ReducerResult(
                    old.copy(status = PostStatus.NextPageLoading),
                    PostEffect.LoadNextPage(nextId, PAGE_SIZE),
                )
            }
        }

        PostMessage.LoadPrevPage -> {
            val prevId = old.posts.firstOrNull()?.id
            if (prevId == null || old.status !is PostStatus.Idle) {
                ReducerResult(old)
            } else {
                ReducerResult(
                    old.copy(status = PostStatus.PrevPageLoading, skeletons = PAGE_SIZE),
                    PostEffect.LoadPrevPage(prevId, PAGE_SIZE),
                )
            }
        }
        is PostMessage.PrevPageLoaded -> ReducerResult(
            when (message.result) {
                is Either.Left -> {
                    old.copy(status = PostStatus.PrevPageError(message.result.value))
                }

                is Either.Right -> old.copy(
                    posts =  message.result.value + old.posts,
                    status = PostStatus.Idle(message.result.value.size < PAGE_SIZE),
                )
            }
        )

        PostMessage.Retry -> {
            val nextId = old.posts.lastOrNull()?.id
            if (nextId == null) {
                ReducerResult(old)
            } else {
                ReducerResult(
                    old.copy(status = PostStatus.NextPageLoading),
                    PostEffect.LoadNextPage(nextId, PAGE_SIZE),
                )
            }
        }

        PostMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.posts.isEmpty()) {
                    PostStatus.EmptyLoading
                } else {
                    PostStatus.Refreshing
                }
            ),
            PostEffect.LoadInitialPage(count = INITIAL_SIZE),
        )

        is PostMessage.Delete -> ReducerResult(
            old.copy(posts = old.posts.filter { it.id != message.post.id }),
            PostEffect.Delete(message.post),
        )

        is PostMessage.DeleteError -> with(message.error) {
            ReducerResult(
                old.copy(
                    posts = buildList(old.posts.size + 1) {
                        addAll(old.posts.takeWhile { it.id > post.id })
                        add(post)
                        addAll(old.posts.takeLastWhile { it.id < post.id })
                    },
                    singleError = throwable,
                )
            )
        }

        is PostMessage.AudioButtonClick -> ReducerResult(
            old.copy(
                posts = old.posts.map {
                    if (it.id == message.post.id) {
                        it.copy(
                            playedAudio = !it.playedAudio,
                        )
                    } else {
                        it
                    }
                }
            ), action = null)

        is PostMessage.NextPageLoaded -> ReducerResult(
            when (message.result) {
                is Either.Left -> {
                    old.copy(status = PostStatus.NextPageError(message.result.value))
                }

                is Either.Right -> old.copy(
                    posts = old.posts + message.result.value,
                    status = PostStatus.Idle(message.result.value.size < PAGE_SIZE),
                )
            }
        )

        is PostMessage.LikeResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    val value = result.value
                    val post = value.post
                    old.copy(
                        posts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        },
                        singleError = value.throwable,
                    )
                }

                is Either.Right -> {
                    val post = result.value
                    old.copy(
                        posts = old.posts.map {
                            if (it.id == post.id) {
                                post
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        )

        PostMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is PostMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> {
                    if (old.posts.isEmpty()) {
                        old.copy(status = PostStatus.EmptyError(result.value))
                    } else {
                        old.copy(singleError = result.value)
                    }
                }

                is Either.Right -> old.copy(
                    posts = result.value,
                    status = PostStatus.Idle(result.value.size < INITIAL_SIZE),
                )
            }
        )
    }
}
