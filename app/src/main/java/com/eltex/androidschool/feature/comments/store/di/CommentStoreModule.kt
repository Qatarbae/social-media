package com.eltex.androidschool.feature.comments.store.di

import androidx.lifecycle.SavedStateHandle
import com.eltex.androidschool.feature.comments.CommentReducer
import com.eltex.androidschool.feature.comments.effecthandler.CommentEffectHandler
import com.eltex.androidschool.feature.comments.fragment.CommentFragment
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.store.CommentStore
import com.eltex.androidschool.feature.comments.viewmodel.CommentUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class CommentStoreModule {

    @Provides
    @ViewModelScoped
    fun provideCommentStore(
        reducer: CommentReducer,
        effectHandler: CommentEffectHandler,
        handle: SavedStateHandle
    ): CommentStore = CommentStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(
            CommentMessage.Refresh(
                requireNotNull(handle.get<Long>(CommentFragment.POST_ID_VALUE))
            )
        ),
        initState = CommentUiState(),
    )
}