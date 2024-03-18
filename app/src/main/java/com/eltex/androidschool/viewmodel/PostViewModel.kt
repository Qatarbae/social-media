package com.eltex.androidschool.viewmodel

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.service.AudioPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val store: PostStore, val application: Application
) : ViewModel() {

    val uiState: StateFlow<PostUiState> = store.state
    private var player: MediaController? = null
    private var oldPost: PostUiModel? = null
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

    fun togglePlay(post: PostUiModel) {
        val url = post.attachment?.url.toString()
        if (oldPost != null && post.id != oldPost?.id) {
            if (player?.isPlaying == true) {
                player?.removeMediaItem(0)
            }
            oldPost = post
            setUri(url)
        } else if (oldPost == null) {
            oldPost = post
            setUri(url)
        }
        if (post.id == oldPost?.id) {
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

    fun accept(message: PostMessage) = store.accept(message)
}
