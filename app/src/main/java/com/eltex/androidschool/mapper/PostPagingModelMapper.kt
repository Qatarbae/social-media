package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.PostStatus
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.PostUiState

class PostPagingModelMapper {
    fun map(state: PostUiState): List<PagingModel<PostUiModel>> {
        val posts = state.posts.map {
            PagingModel.Data(it)
        }

        return when (val status = state.status) {
            is PostStatus.NextPageError -> posts + PagingModel.Error(status.reason)
            PostStatus.NextPageLoading -> posts + PagingModel.Loading()

            else -> posts
        }
    }
}
