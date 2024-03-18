package com.eltex.androidschool.repository.job

import com.eltex.androidschool.api.job.JobApi
import com.eltex.androidschool.model.job.MyJob
import javax.inject.Inject

class NetworkJobRepository @Inject constructor(
    private val jobApi: JobApi
): JobRepository {
    override suspend fun saveJob(
        id: Long,
        myJob: MyJob
    ): MyJob {
        return jobApi.save(myJob)
    }
}