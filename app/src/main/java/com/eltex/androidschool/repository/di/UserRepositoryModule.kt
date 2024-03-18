package com.eltex.androidschool.repository.di

import com.eltex.androidschool.repository.NetworkUserRepository
import com.eltex.androidschool.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface UserRepositoryModule {
    @Binds
    fun bindsUserRepository(impl: NetworkUserRepository): UserRepository
}
