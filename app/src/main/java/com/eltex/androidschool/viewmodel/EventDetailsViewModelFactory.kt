package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.EventUiModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EventDetailsViewModelFactory {
    fun create(eventUiModel: EventUiModel): EventDetailsViewModel
}