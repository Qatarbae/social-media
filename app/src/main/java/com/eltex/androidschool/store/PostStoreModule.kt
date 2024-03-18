package com.eltex.androidschool.store

import com.eltex.androidschool.effecthandler.PostEffectHandler
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.reducer.PostReducer
import com.eltex.androidschool.viewmodel.PostStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class PostStoreModule {
    @Provides
    fun providePostStore(
        reducer: PostReducer,
        effectHandler: PostEffectHandler,
    ): PostStore = PostStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(PostMessage.Refresh),
        initState = PostUiState(),
    )
}
