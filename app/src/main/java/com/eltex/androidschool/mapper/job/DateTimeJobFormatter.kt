package com.eltex.androidschool.mapper.job

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateTimeJobFormatter @Inject constructor()  {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

     fun parseDate(dateString: String): Instant? {
        return try {
            if (dateString.isNotEmpty()) {
                val localDate = LocalDate.parse(dateString, formatter)
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}