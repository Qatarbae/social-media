package com.eltex.androidschool.repository.di

import com.eltex.androidschool.repository.job.JobRepository
import com.eltex.androidschool.repository.job.NetworkJobRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface JobRepositoryModule {

    @Binds
    fun bindsJobRepository(impl: NetworkJobRepository): JobRepository
}