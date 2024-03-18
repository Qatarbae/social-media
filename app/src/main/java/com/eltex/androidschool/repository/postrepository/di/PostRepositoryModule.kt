package com.eltex.androidschool.repository.postrepository.di

import com.eltex.androidschool.repository.postrepository.PostRepository
import com.eltex.androidschool.repository.postrepository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface PostRepositoryModule {

    @Binds
    fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository
}
