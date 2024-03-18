package com.eltex.androidschool.viewmodel.job

import com.eltex.androidschool.model.Status
import com.eltex.androidschool.model.job.MyJob

data class NewJobUiState (
    val result: MyJob? = null,
    val status: Status = Status.Idle,
)