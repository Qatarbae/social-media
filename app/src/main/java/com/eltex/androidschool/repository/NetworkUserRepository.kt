package com.eltex.androidschool.repository

import android.content.ContentResolver
import android.net.Uri
import com.eltex.androidschool.api.UsersApi
import com.eltex.androidschool.datasource.AuthDataSource
import com.eltex.androidschool.model.User
import com.eltex.androidschool.model.UserToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NetworkUserRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val contentResolver: ContentResolver,
    private val authSource: AuthDataSource,
) : UserRepository {

    override suspend fun registration(
        login: String,
        pass: String,
        name: String,
        fileUri: Uri?
    ): UserToken {
        val avatar = fileUri?.let {
            MultipartBody.Part.createFormData(
                "fileUri",
                "fileUri",
                withContext(Dispatchers.IO) {
                    requireNotNull(fileUri.let { contentResolver.openInputStream(it) }).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                },
            )
        }
        val result = usersApi.registration(
            login = login,
            pass = pass,
            name = name,
            file = avatar,
        )

        authSource.token = result.token
        authSource.userId = result.id

        return result
    }

    override suspend fun getAll(): List<User> = usersApi.getAll()
    override suspend fun getCurrent(id: Long): User = usersApi.getCurrent(id)

}
