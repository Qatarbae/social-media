package com.eltex.androidschool.feature.events

import com.eltex.androidschool.model.EventUiModel

interface EventListener {
    fun onEventDetailsClickListener(event: EventUiModel)
    fun onLikeClickListener(event: EventUiModel)
    fun onShareClickListener(event: EventUiModel)
    fun onDeleteClickListener(event: EventUiModel)
    fun onEditClickListener(event: EventUiModel)
    fun onParticipateClickListener(event: EventUiModel)
    fun onAudioClickListener(event: EventUiModel)
    fun onRetryClickListener()
    fun onRefresh()
    fun loadNextPage()
    fun loadPrevPage()
}