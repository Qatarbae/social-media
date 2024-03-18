package com.eltex.androidschool.viewmodel.job

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NewJobViewModelFactory {
    fun create( jobId: Long): NewJobViewModel
}