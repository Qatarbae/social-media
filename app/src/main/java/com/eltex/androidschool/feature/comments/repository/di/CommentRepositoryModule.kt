package com.eltex.androidschool.feature.comments.repository.di

import com.eltex.androidschool.feature.comments.repository.CommentRepository
import com.eltex.androidschool.feature.comments.repository.NetworkCommentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CommentRepositoryModule {
    @Binds
    fun bindsCommentRepository(impl: NetworkCommentRepository): CommentRepository
}