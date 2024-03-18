package com.eltex.androidschool.adapter

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.AvatarItemBinding
import com.eltex.androidschool.model.AvatarModel

class AvatarViewHolder(
    private val binding: AvatarItemBinding
) : ViewHolder(binding.root) {

    fun bindAvatar(avatarModel: AvatarModel) {
        if (avatarModel.avatar != null) {
            binding.initial.isGone = true

            Glide.with(binding.avatar)
                .load(avatarModel.avatar)
                .transform(CircleCrop())
                .into(binding.avatar)
        } else {
            binding.avatar.setImageResource(R.drawable.avatar_background)
            binding.initial.isVisible = true
            binding.initial.text = avatarModel.name.take(1)
        }
    }
}