package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PostUiModelMapper @Inject constructor() {

    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            attachment = attachment,
            authorAvatar = authorAvatar,
        )
    }
}
