package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.model.AvatarModel

class AvatarItemCallback : DiffUtil.ItemCallback<AvatarModel>() {
    override fun areItemsTheSame(oldItem: AvatarModel, newItem: AvatarModel): Boolean =
        oldItem.userId == newItem.userId

    override fun areContentsTheSame(oldItem: AvatarModel, newItem: AvatarModel): Boolean =
        oldItem.name == newItem.name && oldItem.avatar == newItem.avatar
}