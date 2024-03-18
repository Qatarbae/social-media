package com.eltex.androidschool.repository

import android.content.ContentResolver
import androidx.core.net.toUri
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NetworkEventRepository @Inject constructor(
    private val eventsApi: EventsApi,
    private val mediaApi: MediaApi,
    private val contentResolver: ContentResolver
) : EventRepository {
    override suspend fun getLatest(count: Int): List<Event> = eventsApi.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Event> =
        eventsApi.getBefore(id, count)

    override suspend fun getAfter(id: Long, count: Int): List<Event> =
        eventsApi.getAfter(id, count)

    override suspend fun participateById(id: Long): Event = eventsApi.participate(id)

    override suspend fun unparticipateById(id: Long): Event = eventsApi.unparticipate(id)

    override suspend fun likeById(id: Long): Event = eventsApi.like(id)

    override suspend fun unlikeById(id: Long): Event = eventsApi.dislike(id)
    override suspend fun saveEvent(
        id: Long,
        content: String,
        fileModel: FileModel?,
        coords: Coordinates?
    ): Event {
        val event = fileModel?.let {
            val media = if (it.isUploaded) Media(fileModel.uri) else upload(it)

            Event(
                id = id,
                content = content,
                attachment = Attachment(media.url, it.type),
                coords = coords
            )
        } ?: Event(id = id, content = content, coords = coords)

        return eventsApi.save(event)
    }

    private suspend fun upload(fileModel: FileModel): Media =
        mediaApi.upload(
            MultipartBody.Part.createFormData(
                "file",
                "file",
                withContext(Dispatchers.IO) {
                    requireNotNull(contentResolver.openInputStream(fileModel.uri.toUri())).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                }
            )
        )

    override suspend fun deleteById(id: Long) = eventsApi.delete(id)
}