package com.eltex.androidschool.repository

import android.net.Uri
import com.eltex.androidschool.model.User
import com.eltex.androidschool.model.UserToken

interface UserRepository {
    suspend fun registration(login: String, pass: String, name: String, fileUri: Uri?): UserToken =
        error("Not implemented")

    suspend fun authentification(login: String, pass: String): UserToken = error("Not implemented")
    suspend fun getAll(): List<User> = error("Not implemented")
    suspend fun getCurrent(id: Long): User = error("Not implemented")
}