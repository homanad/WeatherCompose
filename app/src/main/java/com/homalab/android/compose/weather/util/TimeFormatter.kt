package com.homalab.android.compose.weather.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {

    private const val SUN_EVENT_TIME_PATTERN = "hh:mm a"

    private const val FULL_TIME_PATTERN = "EEE, d MMM yyyy HH:mm:ss z"

    private const val CHART_TIME_PATTERN = "HH:mm"

    private const val DETAIL_DAY_PATTERN = "dd MMM"

    private const val ONLY_DAY_PATTERN = "dd MMM yyyy"

    private fun formatTime(unixSeconds: Long, shiftTimeZoneSeconds: Int, pattern: String): String {
        val date = Date((unixSeconds - shiftTimeZoneSeconds) * 1000)
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

        return dateFormat.format(date)
    }

    fun formatFullTime(unixSeconds: Long, shiftTimeZoneSeconds: Int) =
        formatTime(unixSeconds, shiftTimeZoneSeconds, FULL_TIME_PATTERN)

    fun formatSunEventTime(unixSeconds: Long, shiftTimeZoneSeconds: Int) =
        formatTime(unixSeconds, shiftTimeZoneSeconds, SUN_EVENT_TIME_PATTERN)

    fun formatChartTime(unixSeconds: Long, shiftTimeZoneSeconds: Int) =
        formatTime(unixSeconds, shiftTimeZoneSeconds, CHART_TIME_PATTERN)

    fun formatDetailDayTime(unixSeconds: Long, shiftTimeZoneSeconds: Int) =
        formatTime(unixSeconds, shiftTimeZoneSeconds, DETAIL_DAY_PATTERN)

    fun formatDetailDay(unixSeconds: Long, shiftTimeZoneSeconds: Int) =
        formatTime(unixSeconds, shiftTimeZoneSeconds, ONLY_DAY_PATTERN)

    fun getStartOfForecastTime(unixSeconds: Long, shiftTimeZoneSeconds: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (unixSeconds - shiftTimeZoneSeconds) * 1000
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        return calendar.timeInMillis
    }
}