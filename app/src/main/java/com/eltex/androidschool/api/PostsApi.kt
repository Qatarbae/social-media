package com.eltex.androidschool.api

import com.eltex.androidschool.model.Post
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {
    @GET("api/posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Long, @Query("count") count: Int): List<Post>

    @GET("api/posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Long, @Query("count") count: Int): List<Post>

    @GET("api/posts/latest")
    suspend fun getLatest(@Query("count") count: Int): List<Post>

    @POST("api/posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Post

    @DELETE("api/posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Post

    @POST("api/posts")
    suspend fun save(@Body post: Post): Post

    @DELETE("api/posts/{id}")
    suspend fun deleteById(@Path("id") id: Long)
}
