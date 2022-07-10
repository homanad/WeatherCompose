package com.homalab.android.compose.weather.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {

    private const val SUN_EVENT_TIME_PATTERN = "hh:mm a"

    private const val FULL_TIME_PATTERN = "EEE, d MMM yyyy HH:mm:ss z"

    private const val CHART_TIME_PATTERN = "HH:mm"

//    private const val ONLY_DAY_PATTERN = "dd MM yyyy z"

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

//    fun formatForecastTimeString(unixSeconds: Long, shiftTimeZoneSeconds: Int): String {
//        val date = Date((unixSeconds - shiftTimeZoneSeconds) * 1000)
//        val dateFormat = SimpleDateFormat(ONLY_DAY_PATTERN, Locale.getDefault())
//
//        return dateFormat.format(date)
//    }

    fun formatChartTime(unixSeconds: Long, shiftTimeZoneSeconds: Int): String {
        val date = Date((unixSeconds - shiftTimeZoneSeconds) * 1000)
        val dateFormat = SimpleDateFormat(CHART_TIME_PATTERN, Locale.getDefault())

        return dateFormat.format(date)
    }

    fun formatForecastTime(unixSeconds: Long, shiftTimeZoneSeconds: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (unixSeconds - shiftTimeZoneSeconds) * 1000
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        return calendar.timeInMillis
    }
}