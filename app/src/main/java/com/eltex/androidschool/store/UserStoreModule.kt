package com.eltex.androidschool.store

import com.eltex.androidschool.effecthandler.UserEffectHandler
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.model.UserUiState
import com.eltex.androidschool.reducer.UserReducer
import com.eltex.androidschool.viewmodel.UserStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class UserStoreModule {
    @Provides
    fun provideUserStore(
        reducer: UserReducer,
        effectHandler: UserEffectHandler,
    ): UserStore = UserStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(UserMessage.Refresh),
        initState = UserUiState(),
    )
}
