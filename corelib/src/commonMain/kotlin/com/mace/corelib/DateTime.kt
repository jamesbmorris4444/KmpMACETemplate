package com.mace.corelib

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateTime {

    fun getFormattedDate(
        iso8601Timestamp: String,
    ): String {
        val localDateTime = iso8601TimestampToLocalDateTime(iso8601Timestamp)
        val date = localDateTime.date
        val day = date.dayOfMonth
        val month = date.monthNumber
        val year = date.year
        return "${zeroPrefixed(day)}.${zeroPrefixed(month)}.${year}"
    }

    private fun iso8601TimestampToLocalDateTime(timestamp: String): LocalDateTime {
        return Instant.parse(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private fun zeroPrefixed(date: Int): String {
        return if (date < 10) {
            "0$date"
        } else {
            date.toString()
        }
    }
}