package com.eltex.androidschool.repository.job

import com.eltex.androidschool.model.job.MyJob

interface JobRepository {

    suspend fun saveJob(id: Long, myJob: MyJob): MyJob =
        error("Not implemented")
}