package com.eltex.androidschool.feature.comments.repository

import com.eltex.androidschool.feature.comments.model.Comment

interface CommentRepository {
    suspend fun getComments(postId: Long): List<Comment> = error("Not implemented")
    suspend fun likeById(postId: Long, commentId: Long): Comment = error("Not implemented")
    suspend fun unlikeById(postId: Long, commentId: Long): Comment = error("Not implemented")
    suspend fun deleteById(postId: Long, commentId: Long): Unit = error("Not implemented")
    suspend fun save(postId: Long, id: Long, content: String): Comment = error("Not implemented")
}
