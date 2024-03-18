package com.eltex.androidschool.feature.comments.api

import com.eltex.androidschool.feature.comments.model.Comment
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {

    @GET("api/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Long): List<Comment>

    @POST("api/posts/{postId}/comments/{id}/likes")
    suspend fun likeById(@Path("postId") postId: Long, @Path("id") commentId: Long): Comment

    @DELETE("api/posts/{postId}/comments/{id}/likes")
    suspend fun unlikeById(@Path("postId") postId: Long, @Path("id") commentId: Long): Comment

    @DELETE("api/posts/{postId}/comments/{id}")
    suspend fun deleteById(@Path("postId") postId: Long, @Path("id") commentId: Long)

    @POST("api/posts/{postId}/comments")
    suspend fun save(@Path("postId") postId: Long, @Body comment: Comment): Comment
}