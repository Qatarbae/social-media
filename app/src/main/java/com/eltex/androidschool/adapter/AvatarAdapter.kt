package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.AvatarItemBinding
import com.eltex.androidschool.model.AvatarModel

class AvatarAdapter(
    private val listener: AvatarListener
) : ListAdapter<AvatarModel, AvatarViewHolder>(AvatarItemCallback()) {

    interface AvatarListener {
        fun onItemsClickListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AvatarItemBinding.inflate(inflater, parent, false)
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bindAvatar(getItem(position))
        //TODO holder.itemView.setOnClickListener
    }
}