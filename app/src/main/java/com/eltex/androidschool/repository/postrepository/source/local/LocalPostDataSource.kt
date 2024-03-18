package com.eltex.androidschool.repository.postrepository.source.local

import com.eltex.androidschool.mapper.PostEntityMapper
import com.eltex.androidschool.model.Post
import javax.inject.Inject

class LocalPostDataSource @Inject constructor(
    private val dao: PostDao,
    private val mapper: PostEntityMapper,
) {
    suspend fun getPostsFromLocalDb(): List<Post> = dao.getAll()
        .map {
            mapper.toPost(it)
        }

    suspend fun savePostsToLocalDb(posts: List<Post>) {
        dao.savePosts(posts.map {
            mapper.toEntity(it)
        })
    }

    suspend fun deleteAllFromLocalDb() = dao.deleteAll()
}