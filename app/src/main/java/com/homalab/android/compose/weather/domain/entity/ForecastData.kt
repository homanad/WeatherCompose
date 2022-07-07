package com.homalab.android.compose.weather.domain.entity

import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastCity
import com.homalab.android.compose.weather.domain.entity.subEntity.ForecastItem

data class ForecastData(
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: ForecastCity
)
