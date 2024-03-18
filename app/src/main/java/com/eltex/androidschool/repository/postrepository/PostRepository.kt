package com.eltex.androidschool.repository.postrepository

import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Post


interface PostRepository {
    suspend fun getBefore(id: Long, count: Int): List<Post> = error("Not implemented")
    suspend fun getAfter(id: Long, count: Int): List<Post> = error("Not implemented")
    suspend fun getLatest(count: Int): List<Post> = error("Not implemented")
    suspend fun likeById(id: Long): Post = error("Not implemented")
    suspend fun unlikeById(id: Long): Post = error("Not implemented")
    suspend fun savePost(
        id: Long,
        content: String,
        fileModel: FileModel?,
        coordinates: Coordinates?,
        mentionIds: List<Long>
    ): Post =
        error("Not implemented")

    suspend fun deleteById(id: Long): Unit = error("Not implemented")
    suspend fun getPostsFromLocalDb(): List<Post> = error("Not implemented")
}
