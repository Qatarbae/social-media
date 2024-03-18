package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.postrepository.source.local.PostEntity
import com.eltex.androidschool.util.PostEntityConverter
import javax.inject.Inject

class PostEntityMapper @Inject constructor(private val converter: PostEntityConverter) {

    fun toEntity(post: Post): PostEntity = with(post) {
        PostEntity(
            id = id,
            content = content,
            author = author,
            authorAvatar = authorAvatar,
            published = converter.fromPublished(published),
            likedByMe = likedByMe,
            likeOwnerIds = converter.fromLikeOwnerIds(likeOwnerIds),
            attachment = attachment?.url,
            attachmentType = converter.fromAttachment(attachment)
        )
    }

    fun toPost(postEntity: PostEntity): Post = with(postEntity) {
        Post(
            id = id,
            content = content,
            author = author,
            authorAvatar = authorAvatar,
            published = converter.toPublished(published),
            likedByMe = likedByMe,
            likeOwnerIds = converter.toLikeOwnerIds(likeOwnerIds),
            attachment = converter.toAttachment(attachment, attachmentType)
        )
    }

}

