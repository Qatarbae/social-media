package com.eltex.androidschool.di

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateTimeFormatter @Inject constructor(
    private val zoneId: ZoneId
) {
    fun format(formatDate: Instant): String = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        .format(formatDate.atZone(zoneId))
}