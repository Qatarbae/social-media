package com.eltex.androidschool.effecthandler

import com.eltex.androidschool.mapper.UserUiModelMapper
import com.eltex.androidschool.model.UserEffect
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.UserRepository
import com.eltex.androidschool.util.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException
import javax.inject.Inject

class UserEffectHandler @Inject constructor(
    private val repository: UserRepository,
    private val mapper: UserUiModelMapper,
) : EffectHandler<UserEffect, UserMessage> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connect(effects: Flow<UserEffect>): Flow<UserMessage> =
        listOf(
            effects.filterIsInstance<UserEffect.LoadInitialPage>()
                .mapLatest {
                    UserMessage.InitialLoaded(
                        try {
                            Either.Right(
                                repository.getAll()
                                    .map(mapper::map)
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(e)
                        }
                    )
                },
        )
            .merge()
}
