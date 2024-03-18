package com.eltex.androidschool.feature.events

import com.eltex.androidschool.model.EventUiModel

interface EventNavigationListener {
    fun onEditClickListener(dest: Int, event: EventUiModel): Unit = error("Not implemented")

    fun onEventDetailsClickListener(dest: Int, event: EventUiModel): Unit = error("Not implemented")
}