package com.homalab.android.compose.weather.presentation.mapper

import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastCity
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem

data class ForecastDayData(
    val city: ForecastCity,
    val items: List<ForecastDayItem>
)

data class ForecastDayItem(
    val dt: Long,
    val list: List<ForecastItem>
)