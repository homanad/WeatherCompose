package com.homalab.android.compose.weather.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {

    private const val SUN_EVENT_TIME_PATTERN = "hh:mm a"

    private const val FULL_TIME_PATTERN = "EEE, d MMM yyyy HH:mm:ss z"

    fun formatFullTime(unixSeconds: Long, shiftTimeZoneSeconds: Int): String {
        val date = Date((unixSeconds - shiftTimeZoneSeconds) * 1000)
        val dateFormat = SimpleDateFormat(FULL_TIME_PATTERN, Locale.getDefault())

        return dateFormat.format(date)
    }

    fun formatSunEventTime(unixSeconds: Long, shiftTimeZoneSeconds: Int): String {
        val date = Date((unixSeconds - shiftTimeZoneSeconds) * 1000)
        val dateFormat = SimpleDateFormat(SUN_EVENT_TIME_PATTERN, Locale.getDefault())

        return dateFormat.format(date)
    }
}