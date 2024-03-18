package com.eltex.androidschool.repository.postrepository

import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.postrepository.source.local.LocalPostDataSource
import com.eltex.androidschool.repository.postrepository.source.network.NetworkPostDataSource
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val networkPostDataSource: NetworkPostDataSource,
    private val localPostDataSource: LocalPostDataSource,
) : PostRepository {

    override suspend fun getBefore(id: Long, count: Int): List<Post> =
        networkPostDataSource.getBefore(id, count)

    override suspend fun getAfter(id: Long, count: Int): List<Post> =
        networkPostDataSource.getAfter(id, count)

    override suspend fun getLatest(count: Int): List<Post> {
        val result = networkPostDataSource.getLatest(count)
        savePostsToLocalDb(result)
        return result
    }

    override suspend fun likeById(id: Long): Post = networkPostDataSource.likeById(id)

    override suspend fun savePost(
        id: Long,
        content: String,
        fileModel: FileModel?,
        coordinates: Coordinates?,
        mentionIds: List<Long>
    ): Post =
        networkPostDataSource.savePost(id, content, fileModel, coordinates, mentionIds)

    override suspend fun deleteById(id: Long) = networkPostDataSource.deleteById(id)

    override suspend fun unlikeById(id: Long): Post = networkPostDataSource.unlikeById(id)

    override suspend fun getPostsFromLocalDb(): List<Post> {
        return localPostDataSource.getPostsFromLocalDb().ifEmpty {
            throw NullPointerException("No saved posts")
        }
    }

    private suspend fun savePostsToLocalDb(posts: List<Post>) {
        localPostDataSource.deleteAllFromLocalDb()
        localPostDataSource.savePostsToLocalDb(posts)
    }
}
