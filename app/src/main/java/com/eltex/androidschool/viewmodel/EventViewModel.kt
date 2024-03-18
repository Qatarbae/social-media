package com.eltex.androidschool.viewmodel

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.service.AudioPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val store: EventStore, val application: Application
) : ViewModel() {

    val state: StateFlow<EventUiState> = store.state
    private var player: MediaController? = null
    private var oldEvent: EventUiModel? = null
    private val playerReady = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val sessionToken =
                SessionToken(
                    application.applicationContext,
                    ComponentName(application.applicationContext, AudioPlayerService::class.java)
                )
            player =
                MediaController.Builder(application.applicationContext, sessionToken).buildAsync()
                    .await()
            playerReady.value = true
            store.connect()
        }
    }

    private fun setUri(uri: String) = viewModelScope.launch {
        playerReady.first { it }
        val mediaItem = MediaItem.fromUri(uri)
        player?.let { player ->
            player.setMediaItem(mediaItem)
            player.prepare()
        }
    }

    fun togglePlay(event: EventUiModel) {
        val url = event.attachment?.url.toString()
        if (oldEvent != null && event.id != oldEvent?.id) {
            if (player?.isPlaying == true) {
                player?.removeMediaItem(0)
            }
            oldEvent = event
            setUri(url)
        } else if (oldEvent == null) {
            oldEvent = event
            setUri(url)
        }
        if (event.id == oldEvent?.id) {
            player?.removeMediaItem(1)
            if (player?.mediaItemCount == 0) {
                setUri(url)
            }
        }
        if (player?.isPlaying == true) pause() else play()
    }

    private fun play() {
        player?.play()
    }

    private fun pause() {
        player?.pause()
    }

    override fun onCleared() {
        player?.release()
        super.onCleared()
    }

    fun accept(eventMessage: EventMessage) {
        store.accept(eventMessage)
    }
}