package com.eltex.androidschool.repository.postrepository.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "authorAvatar")
    val authorAvatar: String? = null,
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "likeOwnerIds")
    val likeOwnerIds: String? = null,
    @ColumnInfo(name = "attachment")
    val attachment: String? = null,
    @ColumnInfo(name = "attachmentType")
    val attachmentType: String? = null,
)

