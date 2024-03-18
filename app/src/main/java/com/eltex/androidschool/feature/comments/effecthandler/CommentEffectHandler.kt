package com.eltex.androidschool.feature.comments.effecthandler

import com.eltex.androidschool.feature.comments.mapper.CommentUiModelMapper
import com.eltex.androidschool.feature.comments.model.CommentEffect
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.model.CommentWithError
import com.eltex.androidschool.feature.comments.repository.CommentRepository
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.util.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CommentEffectHandler @Inject constructor(
    private val repository: CommentRepository,
    private val mapper: CommentUiModelMapper,
) : EffectHandler<CommentEffect, CommentMessage> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connect(effects: Flow<CommentEffect>): Flow<CommentMessage> =
        listOf(
            effects.filterIsInstance<CommentEffect.LoadInitialPage>()
                .mapLatest {
                    CommentMessage.InitialLoaded(
                        try {
                            Either.Right(
                                repository.getComments(it.postId)
                                    .map(mapper::map)
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(e)
                        }
                    )
                },
//            listOf(
//                effects.filterIsInstance<CommentEffect.LoadInitialPage>()
//            )
//                .merge()
//                .filterNotNull(),
            effects.filterIsInstance<CommentEffect.Like>()
                .mapLatest {
                    CommentMessage.LikeResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.comment.likedByMe) {
                                        repository.unlikeById(it.postId, it.comment.id)
                                    } else {
                                        repository.likeById(it.postId, it.comment.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(CommentWithError(it.comment, e))
                        }
                    )
                },
            effects.filterIsInstance<CommentEffect.Create>()
                .mapLatest {
                    CommentMessage.CreateResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    repository.save(it.postId, it.comment.id, it.comment.content)
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(CommentWithError(it.comment, e))
                        }
                    )
                },
            effects.filterIsInstance<CommentEffect.Delete>()
                .mapLatest {
                    try {
                        repository.deleteById(it.postId, it.comment.id)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        CommentMessage.DeleteError(CommentWithError(it.comment, e))
                    }
                }
                .filterIsInstance<CommentMessage.DeleteError>(),
        )
            .merge()
}