package com.homalab.android.compose.weather.presentation.mapper

import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem
import com.homalab.android.compose.weather.util.TimeFormatter


fun ForecastData.toForecastDayData(): ForecastDayData {
    val map = mutableMapOf<Long, MutableList<ForecastItem>>()
    list.forEach {
        val time = TimeFormatter.formatForecastTime(it.dt, city.timeZone)
        if (map[time] == null) {
            map[time] = mutableListOf(it)
        } else {
            map[time]?.add(it)
        }
    }
    val forecastDayItem = map.keys.map {
        val itemList = map[it]!!
        ForecastDayItem(itemList[0].dt, city.timeZone, itemList)
    }
    return ForecastDayData(city, forecastDayItem)
}