package com.eltex.androidschool.model

import com.eltex.androidschool.util.Either

sealed interface PostMessage {
    data object LoadNextPage : PostMessage
    data object LoadPrevPage : PostMessage
    data object Retry : PostMessage
    data object Refresh : PostMessage
    data class Like(val post: PostUiModel) : PostMessage
    data class Delete(val post: PostUiModel) : PostMessage
    data class AudioButtonClick(val post: PostUiModel) : PostMessage
    data object HandleError : PostMessage

    data class DeleteError(val error: PostWithError) : PostMessage
    data class LikeResult(val result: Either<PostWithError, PostUiModel>) : PostMessage
    data class InitialLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
    data class NextPageLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
    data class PrevPageLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostMessage
}
