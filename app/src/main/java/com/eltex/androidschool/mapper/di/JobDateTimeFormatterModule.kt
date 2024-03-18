package com.eltex.androidschool.mapper.di

import com.eltex.androidschool.mapper.job.DateTimeJobFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId

@InstallIn(SingletonComponent::class)
@Module
class JobDateTimeFormatterModule {

    @Provides
    fun provideDateFormatter(): DateTimeJobFormatter {
        return DateTimeJobFormatter()
    }
}