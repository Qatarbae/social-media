package com.eltex.androidschool.api.job

import com.eltex.androidschool.model.job.MyJob
import retrofit2.http.Body
import retrofit2.http.POST

interface JobApi {

    @POST("api/my/jobs")
    suspend fun save(@Body myJob: MyJob): MyJob
}