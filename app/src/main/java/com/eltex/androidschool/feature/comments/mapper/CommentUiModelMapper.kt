package com.eltex.androidschool.feature.comments.mapper

import com.eltex.androidschool.feature.comments.model.Comment
import com.eltex.androidschool.feature.comments.model.CommentUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CommentUiModelMapper @Inject constructor() {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(comment: Comment): CommentUiModel = with(comment) {
        CommentUiModel(
            id = id,
            content = content,
            author = author,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            authorAvatar = authorAvatar,
        )
    }
}
