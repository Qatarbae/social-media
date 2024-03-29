package com.eltex.androidschool.repository.postrepository.source.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 2)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
}