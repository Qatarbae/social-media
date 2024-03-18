package com.eltex.androidschool.repository.postrepository.source.local


import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDbModule {
    @Provides
    fun providePostDao(appDatabase: AppDb): PostDao {
        return appDatabase.postDao()
    }

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext appContext: Context): AppDb {
        return Room.databaseBuilder(
            appContext,
            AppDb::class.java,
            "app_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}


