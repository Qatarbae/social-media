package com.eltex.androidschool.feature.comments.repository

import com.eltex.androidschool.feature.comments.api.CommentApi
import com.eltex.androidschool.feature.comments.model.Comment
import javax.inject.Inject

class NetworkCommentRepository @Inject constructor(private val api: CommentApi) :
    CommentRepository {
    override suspend fun getComments(postId: Long): List<Comment> = api.getComments(postId)

    override suspend fun likeById(postId: Long, commentId: Long): Comment =
        api.likeById(postId, commentId)

    override suspend fun unlikeById(postId: Long, commentId: Long): Comment =
        api.unlikeById(postId, commentId)

    override suspend fun deleteById(postId: Long, commentId: Long) =
        api.deleteById(postId, commentId)

    override suspend fun save(postId: Long, id: Long, content: String): Comment = api.save(
        postId,
        Comment(
            id = id,
            postId = postId,
            content = content,
        )
    )


}