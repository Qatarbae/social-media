package com.eltex.androidschool.api

import com.eltex.androidschool.model.User
import com.eltex.androidschool.model.UserToken
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UsersApi {
    @Multipart
    @POST("api/users/registration")
    suspend fun registration(
        @Part("login") login: String,
        @Part("pass") pass: String,
        @Part("name") name: String,
        @Part file: MultipartBody.Part?
    ): UserToken

    @GET("api/users")
    suspend fun getAll(): List<User>

    @GET("api/users/{id}")
    suspend fun getCurrent(@Path("id") id: Long): User
}
