package com.eltex.androidschool.repository.postrepository.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PostDao {
    /**
     * Возвращает все сохраненные в БД посты
     */
    @Query("SELECT * FROM Posts ORDER BY id DESC")
    suspend fun getAll(): List<PostEntity>

    /**
     * Сохраняет список постов в БД
     *
     */
    @Upsert
    suspend fun savePosts(posts: List<PostEntity>)

    /**
     * Удаляет все посты из таблицы
     */
    @Query("DELETE FROM Posts")
    suspend fun deleteAll()
}