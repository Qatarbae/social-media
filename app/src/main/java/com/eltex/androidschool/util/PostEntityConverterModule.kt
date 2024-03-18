package com.eltex.androidschool.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostEntityConverterModule {
    @Provides
    @Singleton
    fun provideDateTimeFormatter(): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

    @Provides
    @Singleton
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()
}