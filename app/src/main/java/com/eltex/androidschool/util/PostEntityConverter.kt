package com.eltex.androidschool.util

import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.AttachmentType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostEntityConverter @Inject constructor(private val timeZoneId: ZoneId) {
    /**
     * @return экземпляр класса DateTimeFormatter
     */
    private fun getFormatter(): DateTimeFormatter =
        PostEntityConverterModule.provideDateTimeFormatter()

    /**
     * @param dateTimeToFormat
     * Преобразовывает объект типа Instant в объект типа String
     */
    fun fromPublished(dateTimeToFormat: Instant): String =
        getFormatter().format(dateTimeToFormat.atZone(timeZoneId))

    /**
     * @param dateTimeToFormat
     * Преобразовывает объект типа String в Instant
     */
    fun toPublished(dateTimeToFormat: String): Instant {
        val dateTimeString = LocalDateTime.parse(dateTimeToFormat, getFormatter())
        return dateTimeString.atZone(timeZoneId).toInstant()
    }

    /**
     * @param likeOwnerIds
     * Преобразовывает объект типа Set<Long> в String?
     */
    fun fromLikeOwnerIds(likeOwnerIds: Set<Long>): String? {
        return when (likeOwnerIds) {
            emptySet<Long>() -> null
            else -> likeOwnerIds.joinToString(','.toString())
        }
    }

    /**
     * @param likeOwnerIds
     * Преобразовывает объект типа String? в Set<Long>
     */
    fun toLikeOwnerIds(likeOwnerIds: String?): Set<Long> {
        return if (likeOwnerIds.isNullOrBlank()) emptySet()
        else likeOwnerIds.filter { !it.isWhitespace() }
            .split(',').map { it.toLong() }.toSet()
    }

    /**
     * @param attachment
     * Преобразовывает объект типа Attachment? в String?
     */
    fun fromAttachment(attachment: Attachment?): String? {
        return attachment?.let {
            it.attachmentType.toString()
        }
    }

    /**
     * @param attachmentUrl
     * @param attachmentType
     * Преобразовывает объекты типа String? в Attachment?
     */
    fun toAttachment(attachmentUrl: String?, attachmentType: String?): Attachment? {
        return attachmentUrl?.let {
            Attachment(attachmentUrl.toString(), AttachmentType.valueOf(attachmentType.toString()))
        }
    }


}