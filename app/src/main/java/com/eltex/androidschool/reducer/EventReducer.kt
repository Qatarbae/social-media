package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.util.Either
import javax.inject.Inject

class EventReducer @Inject constructor() : Reducer<EventUiState, EventEffect, EventMessage> {
    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_LOAD_SIZE = 3 * PAGE_SIZE
    }

    override fun reduce(
        old: EventUiState,
        message: EventMessage
    ): ReducerResult<EventUiState, EventEffect> = when (message) {
        is EventMessage.Delete -> ReducerResult(
            old.copy(events = old.events.filter { it.id != message.event.id }),
            EventEffect.Delete(message.event)
        )

        is EventMessage.DeleteError -> ReducerResult(
            old.copy(
                events = buildList(old.events.size + 1) {
                    val eventUiModel = message.error.eventUiModel
                    addAll(old.events.takeWhile { it.id > eventUiModel.id })
                    add(eventUiModel)
                    addAll(old.events.takeLastWhile { it.id < eventUiModel.id })
                },
                singleError = message.error.throwable
            )
        )

        is EventMessage.AudioButtonClick -> ReducerResult(
            old.copy(
                events = old.events.map {
                    if (it.id == message.event.id) {
                        it.copy(
                            playedAudio = !it.playedAudio,
                        )
                    } else {
                        it
                    }
                }
            ), action = null)

        EventMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is EventMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> if (old.events.isEmpty()) {
                    old.copy(status = EventStatus.EmptyError(result.value))
                } else {
                    old.copy(singleError = result.value)
                }

                is Either.Right ->
                    old.copy(
                        events = result.value,
                        status = EventStatus.Idle(result.value.size < INITIAL_LOAD_SIZE)
                    )
            }
        )

        is EventMessage.Like -> ReducerResult(
            old.copy(events = old.events.map {
                if (it.id == message.event.id) {
                    message.event.copy(
                        likedByMe = !message.event.likedByMe,
                        likes = if (message.event.likedByMe) {
                            message.event.likes - 1
                        } else {
                            message.event.likes + 1
                        }
                    )
                } else {
                    it
                }
            }),
            EventEffect.Like(message.event)
        )

        is EventMessage.LikeResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> old.copy(
                    events = old.events.map {
                        if (it.id == result.value.eventUiModel.id) {
                            result.value.eventUiModel
                        } else {
                            it
                        }
                    },
                    singleError = result.value.throwable
                )

                is Either.Right -> old.copy(events = old.events.map {
                    if (it.id == result.value.id) {
                        result.value
                    } else {
                        it
                    }
                })
            }

        )

        EventMessage.LoadNextPage -> if (old.status is EventStatus.Idle && !old.status.finished) {
            ReducerResult(
                old.copy(
                    status = EventStatus.NextPageLoading,
                    skeletons = PAGE_SIZE
                ),
                EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE)
            )
        } else {
            ReducerResult(old)
        }

        EventMessage.LoadPrevPage -> {
            val prevId = old.events.firstOrNull()?.id
            if (prevId == null || old.status !is EventStatus.Idle) {
                ReducerResult(old)
            } else {
                ReducerResult(
                    old.copy(status = EventStatus.PrevPageLoading, skeletons = PAGE_SIZE),
                    EventEffect.LoadPrevPage(prevId, PAGE_SIZE),
                )
            }
        }

        is EventMessage.PrevPageLoaded -> ReducerResult(
            when (message.result) {
                is Either.Left -> {
                    old.copy(status = EventStatus.PrevPageError(message.result.value))
                }

                is Either.Right -> old.copy(
                    events = message.result.value + old.events,
                    status = EventStatus.Idle(message.result.value.size < EventReducer.PAGE_SIZE),
                )
            }
        )



        EventMessage.Retry -> ReducerResult(
            old.copy(status = EventStatus.NextPageLoading),
            EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE)
        )

        is EventMessage.NextPageLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> old.copy(
                    status = EventStatus.NextPageError(result.value)
                )

                is Either.Right -> old.copy(
                    events = old.events + result.value,
                    status = EventStatus.Idle(result.value.size < PAGE_SIZE)
                )
            }

        )

        EventMessage.Refresh -> ReducerResult(
            old.copy(
                skeletons = if (old.events.isEmpty()) {
                    INITIAL_LOAD_SIZE
                } else {
                    0
                },
                status = if (old.events.isEmpty()) {
                    EventStatus.InitialLoading
                } else {
                    EventStatus.Refreshing
                },
            ),
            EventEffect.LoadInitialPage(INITIAL_LOAD_SIZE)
        )

        is EventMessage.Participate -> ReducerResult(
            old.copy(
                events = old.events.map {
                    if (it.id == message.event.id) {
                        message.event.copy(
                            participatedByMe = !message.event.participatedByMe,
                            participants = if (message.event.participatedByMe) {
                                message.event.participants - 1
                            } else {
                                message.event.participants + 1
                            }
                        )
                    } else {
                        it
                    }
                }
            ),
            EventEffect.Participate(message.event)
        )

        is EventMessage.ParticipateResult -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> old.copy(
                    events = old.events.map {
                        if (it.id == result.value.eventUiModel.id) {
                            result.value.eventUiModel
                        } else {
                            it
                        }
                    },
                    singleError = result.value.throwable
                )

                is Either.Right -> old.copy(
                    events = old.events.map {
                        if (it.id == result.value.id) {
                            result.value
                        } else {
                            it
                        }
                    }
                )
            }
        )
    }
}