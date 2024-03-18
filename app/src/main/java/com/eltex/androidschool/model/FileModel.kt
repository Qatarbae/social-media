package com.eltex.androidschool.model

data class FileModel(
    val uri: String,
    val type: AttachmentType,
    val isUploaded: Boolean = false
)