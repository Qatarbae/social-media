package com.eltex.androidschool.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventUiModel(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val coords: Coordinates? = null,
    val type: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0, // likersList.size
    val likersList: List<AvatarModel> = emptyList(),
    val speakersList: List<AvatarModel> = emptyList(),
    val participatedByMe: Boolean = false,
    val participants: Int = 0,
    val participantsList: List<AvatarModel> = emptyList(),
    val attachment: Attachment? = null,
    val link: String = "",
    val playedAudio: Boolean = false
) : Parcelable