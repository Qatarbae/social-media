package com.eltex.androidschool.repository.postrepository.source.network

import android.content.ContentResolver
import androidx.core.net.toUri
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Media
import com.eltex.androidschool.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NetworkPostDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    private val postsApi: PostsApi,
    private val mediaApi: MediaApi,
) {
    suspend fun getBefore(id: Long, count: Int): List<Post> = postsApi.getBefore(id, count)

    suspend fun getAfter(id: Long, count: Int): List<Post> = postsApi.getAfter(id, count)

    suspend fun getLatest(count: Int): List<Post> = postsApi.getLatest(count)

    suspend fun likeById(id: Long): Post = postsApi.likeById(id)

    suspend fun savePost(
        id: Long,
        content: String,
        fileModel: FileModel?,
        coordinates: Coordinates?,
        mentionIds: List<Long>
    ): Post {
        val post = fileModel?.let {
            val media = upload(it)
            Post(
                id = id,
                content = content,
                attachment = Attachment(media.url, it.type),
                coords = coordinates,
                mentionIds = mentionIds
            )
        } ?: Post(id = id, content = content, coords = coordinates, mentionIds = mentionIds)
        return postsApi.save(post)
    }

    suspend fun upload(fileModel: FileModel): Media {
        return mediaApi.upload(
            MultipartBody.Part.createFormData(
                "file",
                "file",
                withContext(Dispatchers.IO) {
                    requireNotNull(contentResolver.openInputStream(fileModel.uri.toUri())).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                },
            )
        )
    }

    suspend fun deleteById(id: Long) = postsApi.deleteById(id)

    suspend fun unlikeById(id: Long): Post = postsApi.unlikeById(id)
}
