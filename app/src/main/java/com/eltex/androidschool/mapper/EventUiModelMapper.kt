package com.eltex.androidschool.mapper

import com.eltex.androidschool.di.DateTimeFormatter
import com.eltex.androidschool.model.AvatarModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import javax.inject.Inject

class EventUiModelMapper @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter
) {
    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            authorId = authorId,
            author = author,
            authorJob = authorJob,
            authorAvatar = authorAvatar,
            content = content,
            datetime = dateTimeFormatter.format(datetime),
            published = dateTimeFormatter.format(published),
            coords = coords,
            type = type,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            likersList = users.filter {
                likeOwnerIds.contains(it.key)
            }.map {
                AvatarModel(it.key, it.value.name, it.value.avatar)
            },
            speakersList = users.filter {
                speakerIds.contains(it.key)
            }.map {
                AvatarModel(it.key, it.value.name, it.value.avatar)
            },
            participatedByMe = participatedByMe,
            participants = participantsIds.size,
            participantsList = users.filter {
                participantsIds.contains(it.key)
            }.map {
                AvatarModel(it.key, it.value.name, it.value.avatar)
            },
            attachment = attachment,
            link = link ?: "",
        )
    }
}