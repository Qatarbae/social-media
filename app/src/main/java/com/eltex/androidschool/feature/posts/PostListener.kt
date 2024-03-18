package com.eltex.androidschool.feature.posts

import com.eltex.androidschool.model.PostUiModel

interface PostListener {
    fun onShareClicked(post: PostUiModel)
    fun onLikeClicked(post: PostUiModel)
    fun onDeleteClicked(post: PostUiModel)
    fun onCommentClicked(post: PostUiModel)
    fun onAudioClicked(post: PostUiModel)
    fun onRefresh()
    fun retryClicked()
    fun loadNextPage()
    fun loadPrevPage()
}