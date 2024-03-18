package com.eltex.androidschool.api.di

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.feature.comments.api.CommentApi
import com.eltex.androidschool.api.UsersApi
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.datasource.AuthDataSource
import com.eltex.androidschool.api.job.JobApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    private companion object {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Singleton
    @Provides
    fun provideOkHttp(authSource: AuthDataSource): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader("Api-Key", BuildConfig.API_KEY)
                    .run {
                        if (authSource.token.isNotBlank()) {
                            addHeader("Authorization", authSource.token)
                        } else {
                            this
                        }
                    }
                    .build()
            )
        }
        .let {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            } else {
                it
            }
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://eltex-android.ru/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()

    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create()

    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()

    @Provides
    fun provideCommentApi(retrofit: Retrofit): CommentApi = retrofit.create()

    @Provides
    fun provideJobApi(retrofit: Retrofit): JobApi = retrofit.create()
}
